package com.cmall.familyhas.api;


import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetNeigouAddressInput;
import com.cmall.familyhas.api.result.ApiForGetNeigouAddressResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForGetNeigouAddress extends RootApi<ApiForGetNeigouAddressResult, ApiForGetNeigouAddressInput> {

	@Override
	public ApiForGetNeigouAddressResult Process(ApiForGetNeigouAddressInput inputParam, MDataMap mRequestMap) {
		ApiForGetNeigouAddressResult apiResult = new ApiForGetNeigouAddressResult();
		String uid = inputParam.getUid();
		Map<String, Object> _streetMap = DbUp.upTable("nc_staff_address").dataSqlOne("SELECT area_code FROM nc_staff_address WHERE uid = '"+uid+"'", new MDataMap());
		String _street = (String) _streetMap.get("area_code");
		Map<String, Object> _areaMap = DbUp.upTable("sc_tmp").dataSqlOne("SELECT p_code FROM sc_tmp WHERE code = '"+_street+"'", new MDataMap());
		String _area = (String) _areaMap.get("p_code");
		Map<String, Object> _cityMap = DbUp.upTable("sc_tmp").dataSqlOne("SELECT p_code FROM sc_tmp WHERE code = '"+_area+"'", new MDataMap());
		String _city = (String) _cityMap.get("p_code");
		Map<String, Object> _provinceMap = DbUp.upTable("sc_tmp").dataSqlOne("SELECT p_code FROM sc_tmp WHERE code = '"+_city+"'", new MDataMap());
		String _province = (String) _provinceMap.get("p_code");
		if(_street.length() > 6) {			
			apiResult.setStreet(_street);
			apiResult.setArea(_area);
			apiResult.setCity(_city);
			apiResult.setProvince(_province);
		}else {
			apiResult.setStreet("");
			apiResult.setArea(_street);
			apiResult.setCity(_area);
			apiResult.setProvince(_city);
		}

		return apiResult;
	}
	
}
