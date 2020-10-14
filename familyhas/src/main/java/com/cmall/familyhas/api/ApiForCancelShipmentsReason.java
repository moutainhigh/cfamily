package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiForCancelShipmentsReasonInput;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.familyhas.api.result.ApiForCancelShipmentsReasonResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 取消发货原因
 * 
 * @author jlin
 *
 */
public class ApiForCancelShipmentsReason extends RootApiForManage<ApiForCancelShipmentsReasonResult, ApiForCancelShipmentsReasonInput> {

	public ApiForCancelShipmentsReasonResult Process(ApiForCancelShipmentsReasonInput inputParam, MDataMap mRequestMap) {
		
		ApiForCancelShipmentsReasonResult result = new ApiForCancelShipmentsReasonResult();
		
		List<MDataMap> list=DbUp.upTable("oc_return_goods_reason").queryByWhere("after_sales_type","449747660007","status","449747660005","app_code","SI2003");
		
		if(list!=null){
			for (MDataMap mDataMap : list) {
				String reason_code = mDataMap.get("return_reson_code");
				String reson_content = mDataMap.get("return_reson");
				Reason reason = new Reason(reason_code, reson_content);
				result.getReasonList().add(reason);
			}
		}
		
		return result;
	}

}
