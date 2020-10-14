package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiCancelOrderForIqiyiInput;
import com.cmall.familyhas.api.result.ApiCancelOrderForIqiyiResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.systemcenter.bill.MD5Util;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 取消爱奇艺订单
 * @author jlin
 *
 */
public class ApiCancelOrderForIqiyi extends RootApiForManage<ApiCancelOrderForIqiyiResult,ApiCancelOrderForIqiyiInput> {

	public ApiCancelOrderForIqiyiResult Process(ApiCancelOrderForIqiyiInput input, MDataMap mRequestMap) {
		
		ApiCancelOrderForIqiyiResult result = new ApiCancelOrderForIqiyiResult();
		
		String signature=input.getSignature();
		String out_order_code=input.getOrder_code();
		String buyer_code=bConfig("groupcenter.AQY_code");
		String sign=bConfig("groupcenter.AQY_sign");
		
		
		//验签
		if(!signature.equals(MD5Util.md5Hex(out_order_code+buyer_code+sign))){
			result.setResultCode(916421178);
			result.setResultMessage(bInfo(916421178));
			return result;
		}
		
		//订单数据校验
		List<MDataMap> dataMaps=DbUp.upTable("oc_orderinfo").queryByWhere("out_order_code",out_order_code,"seller_code",MemberConst.MANAGE_CODE_HOMEHAS,"buyer_code",buyer_code,"order_type","449715200012","pay_type","449716200001");
		if(dataMaps==null||dataMaps.size()<1){
			result.setResultCode(916421179);
			result.setResultMessage(bInfo(916421179));
			return result;
		}
		
		StringBuffer order_codes=new StringBuffer();
		for (MDataMap dataMap : dataMaps) {
			String order_code=dataMap.get("order_code");
			order_codes.append(",").append(order_code);
		}
		
		RootResult rr = new OrderService().CancelOrderForList(order_codes.substring(1));
		
		result.setResultCode(rr.getResultCode());
		result.setResultMessage(rr.getResultMessage());
			
		return result;
	}
}