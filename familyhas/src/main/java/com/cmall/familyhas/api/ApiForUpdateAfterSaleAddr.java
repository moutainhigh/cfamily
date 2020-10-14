package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForUpdateAfterSaleAddrInput;
import com.cmall.familyhas.api.result.ApiForUpdateAfterSaleAddrResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 更新售后地址
 * @author jlin
 *
 */
public class ApiForUpdateAfterSaleAddr extends RootApi<ApiForUpdateAfterSaleAddrResult, ApiForUpdateAfterSaleAddrInput> {

	@Override
	public ApiForUpdateAfterSaleAddrResult Process(ApiForUpdateAfterSaleAddrInput inputParam, MDataMap mRequestMap) {

		ApiForUpdateAfterSaleAddrResult  result = new ApiForUpdateAfterSaleAddrResult();

		String uid = inputParam.getUid();
		String asale_code=inputParam.getAsale_code();

		MDataMap addressInfo = DbUp.upTable("oc_address_info").one("uid", uid);
		if (addressInfo != null && !addressInfo.isEmpty()) {
			
			MDataMap updateMap=new MDataMap();
			updateMap.put("return_code",asale_code);
			updateMap.put("mobile",addressInfo.get("after_sale_mobile"));
			updateMap.put("contacts",addressInfo.get("after_sale_person"));
			updateMap.put("address",addressInfo.get("after_sale_address"));
			updateMap.put("receiver_area_code",addressInfo.get("after_sale_postcode"));
			
			DbUp.upTable("oc_return_goods").dataUpdate(updateMap, "mobile,contacts,address,receiver_area_code", "return_code");
		}

		return result;
	}
}
