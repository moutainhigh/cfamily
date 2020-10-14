package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.util.List;

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
 * 退货扣除下单赋予的积分
 */
public class JobForGiveIntegralReturn extends RootJobForExec {

	ReturnGoodsService goodsService = new ReturnGoodsService();
	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();

		MDataMap returnGoods = DbUp.upTable("oc_return_goods").one("return_code", sInfo);
		if (returnGoods == null) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("退货单不存在");
			return mWebResult;
		}
		
		if ("4497153900050006".equals(returnGoods.get("status"))
				|| "4497153900050007".equals(returnGoods.get("status"))) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("退货单已经被客服否决或者已经取消");
			return mWebResult;
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", returnGoods.get("order_code"));

		// LD订单不通过惠家有系统扣除积分
		if ("SI2003".equals(returnGoods.get("small_seller_code"))) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("LD订单不通过惠家有系统扣除积分");
			return mWebResult;
		}
		
		MDataMap changeMap = DbUp.upTable("mc_member_integral_change").one("member_code",orderInfo.get("buyer_code"),"change_type","449748080004","remark",returnGoods.get("order_code"));
		// 检查是否已经赋予过积分
		if(changeMap == null){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("订单尚未赋予积分");
			return mWebResult;
		}
		
		// 检查是否调用过退货接口
		if(DbUp.upTable("mc_member_integral_change").count("member_code", orderInfo.get("buyer_code"), "change_type", "449748080005", "remark", sInfo) > 0){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("已经扣除下单赋予的积分");
			return mWebResult;
		}
		
		// 退货商品部分赋予的积分
		BigDecimal returnIntegralMoney = new BigDecimal(returnGoods.get("expected_return_give_accm_money"));
		if(returnIntegralMoney.compareTo(BigDecimal.ZERO) <= 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("退货商品赋予积分为0");
			return mWebResult;
		}
		
		RootResult rootResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.ZB, returnIntegralMoney, changeMap.get("cust_id"), orderInfo.get("big_order_code"), returnGoods.get("order_code"));

		// 记录积分变更日志
		if (rootResult.getResultCode() == 1) {
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", orderInfo.get("buyer_code"));
			changeDataMap.put("cust_id", changeMap.get("cust_id"));
			changeDataMap.put("change_type", "449748080005");
			changeDataMap.put("change_money", returnIntegralMoney.toString());
			changeDataMap.put("remark", sInfo);
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
			
			//增加积分共享逻辑 rhb 20190315
			List<MDataMap> teamList = plusServiceAccm.getIntegralTeamList(orderInfo.get("buyer_code"),orderInfo.get("create_time"));
			if(null != teamList && !teamList.isEmpty()) {
				for (MDataMap team : teamList) {
					String teamMemberCode = team.get("invitee_code");
					String teamCustId = plusServiceAccm.getCustId(teamMemberCode);
					RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GR, returnIntegralMoney, teamCustId, orderInfo.get("big_order_code"), sInfo);
					// 记录积分变更日志  - 积分共享增加
					if(teamResult.getResultCode() == 1) {
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("member_code", teamMemberCode);
						mDataMap.put("cust_id", teamCustId);
						mDataMap.put("change_type", "449748080007");
						mDataMap.put("change_money", returnIntegralMoney.toString());
						mDataMap.put("remark", sInfo);
						mDataMap.put("create_time", FormatHelper.upDateTime());
						DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
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
		config.setExecType("449746990007");
		config.setMaxExecNumber(5);
		return config;
	}

}
