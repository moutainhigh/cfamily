package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiPaymentTypeAllInput;
import com.cmall.familyhas.api.result.ApiPaymentTypeAllResult;
import com.cmall.familyhas.service.PaymentTypeService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 返回目前惠家有在线支付中,当前调用方支持的所有支付方式
 * @author wz
 *
 */
public class ApiPaymentTypeAll extends RootApiForVersion<ApiPaymentTypeAllResult, ApiPaymentTypeAllInput>{

	public ApiPaymentTypeAllResult Process(ApiPaymentTypeAllInput inputParam,
			MDataMap mRequestMap) {

		ApiPaymentTypeAllResult apiPaymentTypeAllResult = new ApiPaymentTypeAllResult();
		
		// 商品明细
		String sSql = "SELECT d.sku_name FROM oc_orderinfo o,oc_orderdetail d WHERE o.order_code = d.order_code and d.gift_flag = '1' and o.big_order_code = :big_order_code";
		List<Map<String, Object>> list = DbUp.upTable("oc_orderinfo").dataSqlList(sSql, new MDataMap("big_order_code", inputParam.getOrder_code()));
		for(Map<String, Object> map : list) {
			ApiPaymentTypeAllResult.ProductItem item = new ApiPaymentTypeAllResult.ProductItem();
			item.setProductName(map.get("sku_name").toString());
			apiPaymentTypeAllResult.getProductList().add(item);
		}
		
		MDataMap upperMap = DbUp.upTable("oc_orderinfo_upper").onePriLib("big_order_code", inputParam.getOrder_code());
		if(upperMap == null) {
			apiPaymentTypeAllResult.setResultCode(0);
			apiPaymentTypeAllResult.setResultMessage("订单号错误！");
			return apiPaymentTypeAllResult;
		}
		// 应付款
		apiPaymentTypeAllResult.setDueMoney(new BigDecimal(upperMap.get("due_money")));
		
		int count01 = DbUp.upTable("oc_orderinfo").countPri("big_order_code", inputParam.getOrder_code(), "order_status", FamilyConfig.ORDER_STATUS_UNPAY);
		// 只有订单状态是待支付状态才显示支付类型
		if(count01 > 0) {
			if("IOS".equalsIgnoreCase(inputParam.getDeviceType())){
				apiPaymentTypeAllResult.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.IOS, inputParam.getOrder_code()));
			} else if("ANDROID".equalsIgnoreCase(inputParam.getDeviceType())){
				apiPaymentTypeAllResult.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.ANDROID, inputParam.getOrder_code()));
			} else if("WAP".equalsIgnoreCase(inputParam.getDeviceType()) || "WX".equalsIgnoreCase(inputParam.getDeviceType())){
				apiPaymentTypeAllResult.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.WAP, inputParam.getOrder_code()));
			} else if("WEB".equalsIgnoreCase(inputParam.getDeviceType()) || "PC".equalsIgnoreCase(inputParam.getDeviceType())){
				apiPaymentTypeAllResult.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.WEB, inputParam.getOrder_code()));
			} else {
				apiPaymentTypeAllResult.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.ANDROID, inputParam.getOrder_code()));
			}
		}
		
		// 有06状态则返回取消提示
		int count06 = DbUp.upTable("oc_orderinfo").count("big_order_code", inputParam.getOrder_code(), "order_status", FamilyConfig.ORDER_STATUS_TRADE_FAILURE);
		if(count06 > 0) {
			apiPaymentTypeAllResult.setResultMessage("订单已取消！");
		}
		
		// 没有06和01状态则标识已经支付
		if(count01 == 0 && count06 == 0) {
			apiPaymentTypeAllResult.setResultMessage("订单已支付！");
		}

		
		return apiPaymentTypeAllResult;
	}

}
