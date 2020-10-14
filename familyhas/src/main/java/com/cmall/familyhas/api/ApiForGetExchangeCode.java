package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForGetExchangeCodeInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

public class ApiForGetExchangeCode extends RootApiForVersion<RootResult, ApiForGetExchangeCodeInput> {

	@Override
	public RootResult Process(ApiForGetExchangeCodeInput inputParam, MDataMap mRequestMap) {
		String activityCode =  inputParam.getActivity_code();
		MOauthInfo token = getOauthInfo();
		String memberCode = token.getUserCode();
		MDataMap map = DbUp.upTable("oc_coupon_redeem").one("member_code",memberCode,"activity_code",activityCode,"is_redeem","0");
		RootResult result = new RootResult();
		if(map != null && !map.isEmpty()) {
			result.setResultCode(1);
			result.setResultMessage(map.get("activity_cdkey"));
		}else {
			result.setResultCode(1);
			result.setResultMessage("");
		}
		return result;
	}

	

}
