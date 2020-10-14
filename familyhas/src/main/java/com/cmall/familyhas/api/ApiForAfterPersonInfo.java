package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForAfterPersonInfoInput;
import com.cmall.familyhas.api.result.ApiForAfterPersonInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;


public class ApiForAfterPersonInfo extends RootApi<ApiForAfterPersonInfoResult,ApiForAfterPersonInfoInput>{

	public ApiForAfterPersonInfoResult Process(ApiForAfterPersonInfoInput inputParam, MDataMap mRequestMap) {
		ApiForAfterPersonInfoResult apiForAfterPersonInfoResult=new ApiForAfterPersonInfoResult();
		String after_sale_address=inputParam.getMsg_content();
		MDataMap sellerMap=DbUp.upTable("oc_address_info").oneWhere("after_sale_person,after_sale_mobile", "", "after_sale_address=:after_sale_address","after_sale_address", after_sale_address);
		if(sellerMap==null||sellerMap.isEmpty()){
			apiForAfterPersonInfoResult.inErrorMessage(916422111, "");
			return apiForAfterPersonInfoResult;
		}
		apiForAfterPersonInfoResult.setAfter_sale_person(sellerMap.get("after_sale_person"));
		apiForAfterPersonInfoResult.setAfter_sale_mobile(sellerMap.get("after_sale_mobile"));
		return apiForAfterPersonInfoResult;
	}
}
