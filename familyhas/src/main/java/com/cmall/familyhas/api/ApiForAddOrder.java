package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForAddOrderInput;
import com.cmall.familyhas.api.result.ApiForAddOrderResult;
import com.cmall.groupcenter.service.OrderService;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * api 同步订单
 * @author jlin
 *
 */
public class ApiForAddOrder extends
		RootApiForMember<ApiForAddOrderResult, ApiForAddOrderInput> {

	public ApiForAddOrderResult Process(ApiForAddOrderInput input,MDataMap mRequestMap) { 
		
		ApiForAddOrderResult apiForAddOrderResult = new ApiForAddOrderResult();
		
		String orderCode=input.getOrderCode();
		
		//获取mobile
		MDataMap dataMap=DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		if(dataMap==null||dataMap.size()<1){
			
			apiForAddOrderResult.setResultCode(916401225);
			apiForAddOrderResult.setResultMessage(bInfo(916401225, orderCode));
			return apiForAddOrderResult;
		}
		
		String mobileid=new MemberLoginSupport().getMoblie(dataMap.get("buyer_code"));
		
		OrderService orderService=new OrderService();
		if(!orderService.rsyncOrder(orderCode, mobileid)){
			apiForAddOrderResult.setResultCode(916401226);
			apiForAddOrderResult.setResultMessage(bInfo(916401226, orderCode));
		}
		
		return apiForAddOrderResult;
	}
}
