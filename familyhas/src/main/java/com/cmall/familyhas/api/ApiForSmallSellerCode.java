package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForSmallSellerCodeInput;
import com.cmall.familyhas.api.result.ApiForSmallSellerCodeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForSmallSellerCode extends RootApi<ApiForSmallSellerCodeResult,ApiForSmallSellerCodeInput>{

	public ApiForSmallSellerCodeResult Process(ApiForSmallSellerCodeInput inputParam, MDataMap mRequestMap) {
		
		ApiForSmallSellerCodeResult samllSellerCodeResult=new ApiForSmallSellerCodeResult();
		
		String order_code=inputParam.getOrder_code();
		
		MDataMap sellerMap=DbUp.upTable("oc_orderinfo").oneWhere("", "", "order_code=:order_code","order_code", order_code);
		
		if(sellerMap==null||sellerMap.isEmpty()){
			samllSellerCodeResult.inErrorMessage(916422111, order_code);
			return samllSellerCodeResult;
		}
		
		samllSellerCodeResult.setSmall_seller_code(sellerMap.get("small_seller_code"));
		samllSellerCodeResult.setBuyer_mobile((String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code",new MDataMap("member_code", sellerMap.get("buyer_code"))));
		
		return samllSellerCodeResult;
	}



}
