package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForUpdateAfterSaleAddr1Input;
import com.cmall.familyhas.api.result.ApiForUpdateAfterSaleAddr1Result;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 更新售后地址
 * @author jlin
 *
 */
public class ApiForUpdateAfterSaleAddr1 extends RootApi<ApiForUpdateAfterSaleAddr1Result, ApiForUpdateAfterSaleAddr1Input> {

	@Override
	public ApiForUpdateAfterSaleAddr1Result Process(ApiForUpdateAfterSaleAddr1Input inputParam, MDataMap mRequestMap) {

		ApiForUpdateAfterSaleAddr1Result  result = new ApiForUpdateAfterSaleAddr1Result();

		String uid = inputParam.getUid();
		String asale_code=inputParam.getAsale_code();

		MDataMap addressInfo = DbUp.upTable("oc_address_info").one("uid", uid);
		if (addressInfo != null && !addressInfo.isEmpty()) {
			
			MDataMap updateMap=new MDataMap();
			updateMap.put("exchange_no",asale_code);
			updateMap.put("after_sale_mobile",addressInfo.get("after_sale_mobile"));
			updateMap.put("after_sale_person",addressInfo.get("after_sale_person"));
			updateMap.put("after_sale_address",addressInfo.get("after_sale_address"));
			updateMap.put("after_sale_postcode",addressInfo.get("after_sale_postcode"));
			DbUp.upTable("oc_exchange_goods").dataUpdate(updateMap, "", "exchange_no");
		}

		return result;
	}
}
