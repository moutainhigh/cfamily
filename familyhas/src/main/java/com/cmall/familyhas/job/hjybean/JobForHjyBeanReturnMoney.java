package com.cmall.familyhas.job.hjybean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmassystem.modelbean.HjyBeanCtrInput;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrResult;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 订单确认退款时把用户使用的惠豆退还
 */
public class JobForHjyBeanReturnMoney {

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		
		MDataMap returnMoneyMap = DbUp.upTable("oc_return_money").one("return_money_code",sInfo);
		if(returnMoneyMap == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("退款单不存在");
			return mWebResult;
		}
		
		if(!"4497153900040001".equals(returnMoneyMap.get("status"))){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("退款单不是已确认状态");
			return mWebResult;
		}
		
		// 订单使用的惠豆金额
		MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code",returnMoneyMap.get("order_code"),"pay_type","449746280015");
		if(orderPay == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单未使用惠豆");
			return mWebResult;
		}
		
		// 检查是否整单已经退过惠豆
		if(DbUp.upTable("fh_hd_change_detail").count("change_type","449747940004","info",returnMoneyMap.get("order_code")) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单已退还惠豆");
			return mWebResult;
		}
		
		// 检查是否单个退款单已经退过惠豆
		if(DbUp.upTable("fh_hd_change_detail").count("change_type","449747940003","info",sInfo) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此退款单已退还惠豆");
			return mWebResult;
		}
		
		HjybeanService hjybeanService = new HjybeanService();
		
		int amount = 0;
		// 如果没有退货单，则退整单使用的惠豆金额
		if(StringUtils.isBlank(returnMoneyMap.get("return_goods_code"))){
			amount = new BigDecimal(orderPay.get("pay_remark")).divide(hjybeanService.getHomehasBeanConsumeConfig().getRatio(),0, BigDecimal.ROUND_HALF_UP).intValue();
		}else{
			// 有退货单情况下，逐一计算每个退货商品使用的惠豆数
			List<MDataMap> returnSkuMapList = DbUp.upTable("oc_return_goods_detail").queryAll("", "", "", new MDataMap("return_code",returnMoneyMap.get("return_goods_code")));
			BigDecimal money = new BigDecimal(0);
			for(MDataMap m : returnSkuMapList){
				MDataMap dt = DbUp.upTable("oc_orderdetail").one("order_code",returnMoneyMap.get("order_code"),"sku_code",m.get("sku_code"));
				if(dt == null) continue;
				
				// 累加退货的商品使用的惠豆抵扣金额
				money = money.add(new BigDecimal(dt.get("hjy_bean")).multiply(new BigDecimal(m.get("count"))).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			amount = money.divide(hjybeanService.getHomehasBeanConsumeConfig().getRatio(),0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		
		if(amount <= 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("退货的商品未使用惠豆");
			return mWebResult;
		}
		
		HjyBeanCtrInput input = new HjyBeanCtrInput();
		input.setAmount(amount);
		input.setTradetype(3);
		input.setOrderdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		input.setInout(1);
		input.setMemo(sInfo);
		HjyBeanCtrResult result = new HjybeanService().ctrhjyBeanByMemberCode(returnMoneyMap.get("buyer_code"), input);
		
		if(result == null) {
			result = new HjyBeanCtrResult();
			result.setResultCode(0);
			result.setResultMessage("调用接口失败");
		}
		
		if(result.getResultCode() == 1){
			try {
				MDataMap changeDetail = new MDataMap();
				changeDetail.put("change_type", "449747940003");
				changeDetail.put("info", sInfo);
				changeDetail.put("change_amount", ""+amount);
				changeDetail.put("remark", "By JobForHjyBeanReturnMoney");
				changeDetail.put("serialno", result.getCenterserialno());
				changeDetail.put("trade_time", input.getOrderdate());
				changeDetail.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("fh_hd_change_detail").dataInsert(changeDetail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mWebResult.setResultCode(result.getResultCode());
		mWebResult.setResultMessage(result.getResultMessage());
		return mWebResult;
	}

}
