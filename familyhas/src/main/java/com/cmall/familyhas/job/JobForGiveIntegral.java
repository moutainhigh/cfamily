package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.cmall.familyhas.service.ReturnGoodsService;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 签收赋予积分
 */
public class JobForGiveIntegral extends RootJobForExec {

	ReturnGoodsService goodsService = new ReturnGoodsService();
	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();

		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", sInfo);

		if (orderInfo == null) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("未查询到订单信息");
			return mWebResult;
		}
		
		// LD订单不通过惠家有系统赋予积分
		if ("SI2003".equals(orderInfo.get("small_seller_code"))) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("LD订单不通过惠家有系统赋予积分");
			return mWebResult;
		} 
		
		// 家有客代号
		String custId = plusServiceAccm.getCustId(orderInfo.get("buyer_code"));
		if(StringUtils.isBlank(custId)){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("未查询到家有客代号，无法赋予积分");
			return mWebResult;
		}
		
		// 需要赋予的积分金额
		BigDecimal giveMoney = (BigDecimal)DbUp.upTable("oc_orderdetail").dataGet("sum(give_integral_money)", "order_code = :order_code and give_integral_money > 0 and give_integral_money_real = 0", new MDataMap("order_code", sInfo));
		if(giveMoney.compareTo(BigDecimal.ZERO) <= 0){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("没有积分需要赋予");
			return mWebResult;
		}
		
		// 已经确认退货的明细
		BigDecimal returnGiveMoney = (BigDecimal)DbUp.upTable("oc_return_goods").dataGet("sum(expected_return_give_accm_money)", "order_code = :order_code AND `status` = '4497153900050001'", new MDataMap("order_code", sInfo));
		
		// 没有积分需要赋予
		if(returnGiveMoney != null && giveMoney.compareTo(returnGiveMoney) <= 0){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("订单全部退货不赋予积分");
			return mWebResult;
		}
		
		//查询积分共享
		List<MDataMap> teamList = plusServiceAccm.getIntegralTeamList(orderInfo.get("buyer_code"),orderInfo.get("create_time"));
		
		// 赋予积分
		RootResult rootResult = new RootResult();
		if(DbUp.upTable("mc_member_integral_change").count("member_code",orderInfo.get("buyer_code"),"change_type","449748080004","remark",sInfo) == 0){
			rootResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.ZA, giveMoney, custId, orderInfo.get("big_order_code"), sInfo);
			
			// 记录积分变更日志  - 赋予
			if (rootResult.getResultCode() == 1) {
				String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				
				MDataMap changeDataMap = new MDataMap();
				changeDataMap.put("member_code", orderInfo.get("buyer_code"));
				changeDataMap.put("cust_id", custId);
				changeDataMap.put("change_type", "449748080004");
				changeDataMap.put("change_money", giveMoney.toString());
				changeDataMap.put("remark", sInfo);
				changeDataMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
				
				String sql = "update oc_orderdetail set give_integral_money_real = give_integral_money, give_integral_time = :time where order_code = :order_code";
				DbUp.upTable("oc_orderdetail").dataExec(sql, new MDataMap("order_code", sInfo,"time",time));
				
				//增加积分共享逻辑 rhb 20190315
				if(null != teamList && !teamList.isEmpty()) {
					for (MDataMap team : teamList) {
						String teamMemberCode = team.get("invitee_code");
						String teamCustId = plusServiceAccm.getCustId(teamMemberCode);
						RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.G, giveMoney, teamCustId, orderInfo.get("big_order_code"), sInfo);
						// 记录积分变更日志  - 积分共享增加
						if(teamResult.getResultCode() == 1) {
							MDataMap mDataMap = new MDataMap();
							mDataMap.put("member_code", teamMemberCode);
							mDataMap.put("cust_id", teamCustId);
							mDataMap.put("change_type", "449748080007");
							mDataMap.put("change_money", giveMoney.toString());
							mDataMap.put("remark", sInfo);
							mDataMap.put("create_time", FormatHelper.upDateTime());
							DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
						}
						
					}
				}
			}
			
			// 签收接口失败的情况下后续逻辑终止
			if(rootResult.getResultCode() != 1){
				mWebResult.inOtherResult(rootResult);
				return mWebResult;
			}
		}

		// 如果有退货中的商品则还需要再调用一次退货的积分变更接口，保证家有系统中退货的流程一致
		String sSql = "SELECT rg.return_code, rg.expected_return_give_accm_money FROM oc_return_goods rg WHERE rg.`status` NOT IN('4497153900050006','4497153900050007') AND rg.order_code = :order_code AND rg.expected_return_give_accm_money > 0";
		List<Map<String, Object>> returnGoodsList = DbUp.upTable("oc_return_goods").dataSqlList(sSql, new MDataMap("order_code", sInfo));
		
		// 根据退货单号分组的积分金额
		Map<String, BigDecimal> giveMoneyReturnMap = new HashMap<String, BigDecimal>();
		// 最终退货的积分金额，积分保留3位小数
		returnGiveMoney = BigDecimal.ZERO;
		for(Map<String, Object> map : returnGoodsList){
			// 如果已经记录了退货单的日志则忽略此次，防止重复调用退货
			if(DbUp.upTable("mc_member_integral_change").count("member_code",orderInfo.get("buyer_code"),"change_type","449748080005","remark",map.get("return_code")+"") > 0){
				continue;
			}
			
			BigDecimal m = new BigDecimal(map.get("expected_return_give_accm_money")+"");
			returnGiveMoney = returnGiveMoney.add(m);
			giveMoneyReturnMap.put(map.get("return_code")+"", m);
		}
		
		// 退货积分
		if(returnGiveMoney.compareTo(BigDecimal.ZERO) > 0){
			rootResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.ZB, returnGiveMoney, custId, orderInfo.get("big_order_code"), giveMoneyReturnMap.entrySet().iterator().next().getKey());
			
			// 记录积分变更日志  - 退货
			if (rootResult.getResultCode() == 1) {
				String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				
				Set<Entry<String, BigDecimal>> entrySet = giveMoneyReturnMap.entrySet();
				for(Entry<String, BigDecimal> entry : entrySet){
					MDataMap changeDataMap = new MDataMap();
					changeDataMap.put("member_code", orderInfo.get("buyer_code"));
					changeDataMap.put("cust_id", custId);
					changeDataMap.put("change_type", "449748080005");
					changeDataMap.put("change_money", entry.getValue().toString());
					changeDataMap.put("remark", entry.getKey());
					changeDataMap.put("create_time", time);
					DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
				}
				
				//增加积分共享逻辑 rhb 20190315
				if(null != teamList && !teamList.isEmpty()) {
					for (MDataMap team : teamList) {
						String teamMemberCode = team.get("invitee_code");
						String teamCustId = plusServiceAccm.getCustId(teamMemberCode);
						RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GR, giveMoney, teamCustId, orderInfo.get("big_order_code"), sInfo);
						// 记录积分变更日志  - 积分共享增加
						if(teamResult.getResultCode() == 1) {
							MDataMap mDataMap = new MDataMap();
							mDataMap.put("member_code", teamMemberCode);
							mDataMap.put("cust_id", teamCustId);
							mDataMap.put("change_type", "449748080008");
							mDataMap.put("change_money", giveMoney.toString());
							mDataMap.put("remark", sInfo);
							mDataMap.put("create_time", FormatHelper.upDateTime());
							DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
						}
						
					}
				}
			}
		}

		mWebResult.inOtherResult(rootResult);
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990006");
		config.setMaxExecNumber(5);
		return config;
	}

}
