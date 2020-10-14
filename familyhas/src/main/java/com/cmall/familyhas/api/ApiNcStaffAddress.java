package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.cmall.familyhas.api.input.ApiNcStaffAddressInput;
import com.cmall.familyhas.api.result.ApiFamilyConsigneeAddressSelectListResult;
import com.cmall.familyhas.api.result.ApiNcStaffAddressResult;
import com.cmall.ordercenter.service.NcStaffAddress;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 内购地址
 * 
 * @author wz
 * 
 */
public class ApiNcStaffAddress extends
		RootApiForToken<ApiNcStaffAddressResult, ApiNcStaffAddressInput> {

	public ApiNcStaffAddressResult Process(ApiNcStaffAddressInput inputParam,
			MDataMap mRequestMap) {

		ApiNcStaffAddressResult apiNcStaffAddressResult = new ApiNcStaffAddressResult();
		List<ApiFamilyConsigneeAddressSelectListResult> resultList = new ArrayList<ApiFamilyConsigneeAddressSelectListResult>();
		
//		int countPage = 0;
//		
//		String member_code = getUserCode();
		
//		int count = DbUp.upTable("nc_staff_address").dataCount("", new MDataMap());
//		if(count!=0){
//			countPage = count / 10; // 总页数 count
//		}
//		apiNcStaffAddressResult.setCountPage(countPage + 1);
		
		NcStaffAddress nc = new NcStaffAddress();
		List<Map<String, Object>> ncStaffAddressList = nc.queryNcStaffAddressAll();
		
		
		
		for (Map<String, Object> md : ncStaffAddressList) {
			ApiFamilyConsigneeAddressSelectListResult apiFamilyConsigneeAddressSelectListResult = new ApiFamilyConsigneeAddressSelectListResult();
			
			String area_sql="SELECT a.`code` as street_code,c.`name` as prov_name,IF(b.show_yn = 'Y',b.`name`,'') as city_name,d.name as area_name,a.`name` as street_name,b.`code` as city_code,c.`code` as prov_code, d.code as area_code "
					+ "from sc_tmp a  LEFT JOIN sc_tmp b on b.`code`=CONCAT(LEFT(a.`code`,4),'00') "
					+ "LEFT JOIN sc_tmp c on c.`code`=CONCAT(LEFT(a.`code`,2),'0000') "
					+ "LEFT JOIN sc_tmp d on d.code = left(a.code, 6) "
					+ "where a.`code`=:area_code";
			Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",(String)md.get("area_code")));
			if(areaMap!=null && !"".equals(areaMap) && areaMap.size()>0){
				apiFamilyConsigneeAddressSelectListResult.setArea_code((String)areaMap.get("area_code"));
				apiFamilyConsigneeAddressSelectListResult.setArea_name((String)areaMap.get("area_name"));
				apiFamilyConsigneeAddressSelectListResult.setCity_code((String)areaMap.get("city_code"));
				apiFamilyConsigneeAddressSelectListResult.setCity_name((String)areaMap.get("city_name"));
				apiFamilyConsigneeAddressSelectListResult.setProv_code((String)areaMap.get("prov_code"));
				apiFamilyConsigneeAddressSelectListResult.setProv_name((String)areaMap.get("prov_name"));
				apiFamilyConsigneeAddressSelectListResult.setStreet_code((String)areaMap.get("street_code"));
				apiFamilyConsigneeAddressSelectListResult.setStreet_name((String)areaMap.get("street_name"));
			}
			
			apiFamilyConsigneeAddressSelectListResult.setAddress_id(String.valueOf(md
					.get("address_id"))); // 收货人地址id
			apiFamilyConsigneeAddressSelectListResult.setAddress(String.valueOf(md
					.get("address_street"))); // 详细地址
			apiFamilyConsigneeAddressSelectListResult.setMobilephone(String.valueOf(md
					.get("address_mobile"))); // 电话
			apiFamilyConsigneeAddressSelectListResult.setPostcode(String.valueOf(md
					.get("address_postalcode"))); // 邮政编码
			apiFamilyConsigneeAddressSelectListResult.setReceive_person(String.valueOf(md
		 			.get("address_name"))); // 收货人
			/*
			 * 449746250001 默认地址 449746250002 非默认地址
			 */
			apiFamilyConsigneeAddressSelectListResult.setPrice(String.valueOf(md.get("price"))); // 价钱
			
			if ("449746250001".equals(md.get("address_default"))) {
				apiFamilyConsigneeAddressSelectListResult.setFlag_default("1"); // 默认地址
				resultList.add(0,apiFamilyConsigneeAddressSelectListResult);
			} else if ("449746250002".equals(md.get("address_default"))) {
				apiFamilyConsigneeAddressSelectListResult.setFlag_default("0"); // 非默认地址
				resultList.add(apiFamilyConsigneeAddressSelectListResult);
			}

			

		}

		apiNcStaffAddressResult.setApiFamilyConsigneeAddressSelectListResult(resultList);


		return apiNcStaffAddressResult;
	}

}
