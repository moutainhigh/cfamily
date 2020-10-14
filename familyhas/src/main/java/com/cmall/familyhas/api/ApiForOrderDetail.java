package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiForOrderDetailInput;
import com.cmall.familyhas.api.result.ApiForOrderDetailResult;
import com.cmall.familyhas.api.result.ApiForOrderDetailResult.orderDetail;
import com.cmall.membercenter.memberdo.MemberConst;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 后台专用订单详情接口
 * <br>后台不支持细化的接口权限 故暂时不做权限划分
 * @author jlin
 *
 */
public class ApiForOrderDetail extends RootApi<ApiForOrderDetailResult, ApiForOrderDetailInput> {

	public ApiForOrderDetailResult Process(ApiForOrderDetailInput inputParam,MDataMap mRequestMap) {
		
		ApiForOrderDetailResult orderDetailResult = new ApiForOrderDetailResult();
		String order_code=inputParam.getOrder_code();
		
		if(!order_code.startsWith("DD")){
			orderDetailResult.inErrorMessage(916401238, order_code);
			return orderDetailResult;
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code,"delete_flag","0");
		if(orderInfo==null||orderInfo.size()<1){
			
			orderDetailResult.inErrorMessage(916411237, order_code);
			return orderDetailResult;
		}
		
		List<MDataMap> list = DbUp.upTable("oc_orderdetail").queryAll("sku_code,sku_name,sku_num", "", "", new MDataMap("order_code",order_code));
		
		for (MDataMap mDataMap : list) {
			
			orderDetail detail = new orderDetail();
			detail.setSku_code(mDataMap.get("sku_code"));
			detail.setSku_name(mDataMap.get("sku_name"));
			detail.setSku_num(Integer.valueOf(mDataMap.get("sku_num")));
			
			orderDetailResult.getDetails().add(detail);
		}
		
		return orderDetailResult;
	}

	
	
	
	
}
