package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForUserAddressInfoInput;
import com.cmall.familyhas.api.result.ApiForUserAddressInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 返回商户的售后地址[乞丐版]
 * @author jlin
 *
 */
public class ApiForUserAddressInfo extends RootApi<ApiForUserAddressInfoResult, ApiForUserAddressInfoInput> {

	@Override
	public ApiForUserAddressInfoResult Process(ApiForUserAddressInfoInput inputParam, MDataMap mRequestMap) {

		ApiForUserAddressInfoResult addressInfoResult = new ApiForUserAddressInfoResult();

		String uid = inputParam.getUid();

		MDataMap addressInfo = DbUp.upTable("oc_address_info").one("uid", uid);
		if (addressInfo != null && !addressInfo.isEmpty()) {
			addressInfoResult.setAfter_sale_address(addressInfo.get("after_sale_address"));
			addressInfoResult.setAfter_sale_mobile(addressInfo.get("after_sale_mobile"));
			addressInfoResult.setAfter_sale_person(addressInfo.get("after_sale_person"));
			addressInfoResult.setAfter_sale_postcode(addressInfo.get("after_sale_postcode"));
		}

		return addressInfoResult;
	}
}
