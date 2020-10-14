package com.cmall.familyhas.api;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForCancelOrderInput;
import com.cmall.familyhas.api.result.ApiForCancelOrderResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 取消结果页
 * 
 * @author zhouenzhi
 * 
 */
public class ApiForCancelOrderNotice extends
		RootApiForToken<ApiForCancelOrderResult, ApiForCancelOrderInput> {

	public ApiForCancelOrderResult Process(ApiForCancelOrderInput inputParam, MDataMap mRequestMap) {
		ApiForCancelOrderResult result = new ApiForCancelOrderResult();
		String orderCode = inputParam.getOrderCode();
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		MDataMap cancelOrder = DbUp.upTable("oc_order_cancel_h").one("order_code",orderCode);
		result.setCancelNotice("\n" + 
				"\n" + 
				"取消说明：\n" + 
				"\n" + 
				"1、取消成功后，退款会在1-5个工作日，退还到您的支付账号\n" + 
				"\n" + 
				"2、积分、惠币、储值金、暂存款会在取消成功后退还到您的账号\n" + 
				"\n" + 
				"3、商品已经发货会取消失败\n" + 
				"");
		String reasonCode = "";
		String reason = "";
		if(cancelOrder != null && !cancelOrder.isEmpty()) {
			reasonCode = cancelOrder.get("reason_code");
			if(StringUtils.isNotEmpty(reasonCode)) {
				MDataMap reasonMap=DbUp.upTable("oc_return_goods_reason").one("after_sales_type","449747660007","status","449747660005","return_reson_code",reasonCode);
				if(reasonMap !=null && !reasonMap.isEmpty()) {
					reason = reasonMap.get("return_reson");
				}
			}
		}
		result.setCancelReason(reason);
		String cancelStatus = orderInfo != null?orderInfo.get("order_status"):"";
		if("4497153900010008".equals(cancelStatus)) {
			cancelStatus = "等待审核";
		}else if("4497153900010006".equals(cancelStatus)) {
			cancelStatus = "取消成功";
		}else {
			cancelStatus = "等待审核";
		}
		String payType = orderInfo != null?orderInfo.get("pay_type"):"";
		if("449716200001".equals(payType)) {
			payType = "在线支付";
		}else if("449716200002".equals(payType)) {
			payType = "货到付款";
		}else {
			payType = "其他";
		}
		String orderMoney = orderInfo != null?orderInfo.get("order_money"):"0";
		String transportMoney = orderInfo != null?orderInfo.get("transport_money"):"0";
		result.setCancelStatus(cancelStatus);
		result.setOrderCode(orderCode);
		result.setPayType(payType);
		result.setReturnMoney(new BigDecimal(orderMoney).add(new BigDecimal(transportMoney)));
		return result;
	}
	
}
