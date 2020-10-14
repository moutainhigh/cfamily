package com.cmall.familyhas.job.hjybean;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.cmall.familyhas.FamilyConfig;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrInput;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrResult;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * LD的订单取消时退还使用的惠豆
 */
public class JobForHjyBeanOrderCancel {

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		
		MDataMap orderInfoMap = DbUp.upTable("oc_orderinfo").one("order_code",sInfo);
		if(orderInfoMap == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单不存在");
			return mWebResult;
		}
		
		if(!FamilyConfig.ORDER_STATUS_TRADE_FAILURE.equals(orderInfoMap.get("order_status"))){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单状态不是交易失败");
			return mWebResult;
		}
		
		// 使用的惠豆金额
		MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code",sInfo,"pay_type","449746280015");
		if(orderPay == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单未使用惠豆");
			return mWebResult;
		}
		
		// 有退货单
		if(DbUp.upTable("oc_return_goods").dataCount("order_code =:order_code AND status NOT IN('4497153900050002','4497153900050006')", new MDataMap("order_code",sInfo)) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单退货，走退货退惠豆流程");
			return mWebResult;
		}
		
		// 有退款单
		if(DbUp.upTable("oc_return_money").dataCount("order_code =:order_code AND status != '4497153900040002'", new MDataMap("order_code",sInfo)) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单退款，走退款退惠豆流程");
			return mWebResult;
		}
		
		// 检查是否已经退过惠豆
		if(DbUp.upTable("fh_hd_change_detail").count("change_type","449747940004","info",sInfo) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单已退惠豆");
			return mWebResult;
		}
		
		int amount = HjybeanService.reverseRMBToHjyBean(new BigDecimal(orderPay.get("pay_remark"))).intValue();
		if(amount <= 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单使用惠豆数为0");
			return mWebResult;
		}
		
		HjyBeanCtrInput input = new HjyBeanCtrInput();
		input.setAmount(amount);
		input.setTradetype(2);
		input.setOrderdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		input.setInout(1);
		input.setMemo(sInfo);
		HjyBeanCtrResult result = new HjybeanService().ctrhjyBeanByMemberCode(orderInfoMap.get("buyer_code"), input);
		
		if(result == null) {
			result = new HjyBeanCtrResult();
			result.setResultCode(0);
			result.setResultMessage("调用接口失败");
		}
		
		if(result.getResultCode() == 1){
			try {
				MDataMap changeDetail = new MDataMap();
				changeDetail.put("change_type", "449747940004");
				changeDetail.put("info", sInfo);
				changeDetail.put("change_amount", ""+amount);
				changeDetail.put("remark", "By JobForHjyBeanOrderCancel");
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
