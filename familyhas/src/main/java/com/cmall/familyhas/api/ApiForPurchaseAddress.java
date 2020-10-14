package com.cmall.familyhas.api;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForPurchaseAddressInput;
import com.cmall.familyhas.api.result.ApiForPurchaseAddressResult;
import com.cmall.familyhas.model.AddressDetail;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;


public class ApiForPurchaseAddress extends RootApi<ApiForPurchaseAddressResult,  ApiForPurchaseAddressInput>{

	@Override
	public ApiForPurchaseAddressResult Process(ApiForPurchaseAddressInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub\
		ApiForPurchaseAddressResult result = new ApiForPurchaseAddressResult();
		String operateName = inputParam.getOperateName();
		String detail_addtess = inputParam.getDetail_addtess();
		String identity_number = inputParam.getIdentity_number();
		String adress_id = inputParam.getAdress_id();
		String phone = inputParam.getPhone();
		String postcode = inputParam.getPostcode();
		String province_city_district_code = inputParam.getProvince_city_district_code();
		String purchase_order_id = inputParam.getPurchase_order_id();
		String receiver = inputParam.getReceiver();
		String select_flag = inputParam.getSelect_flag();
		String if_delete = inputParam.getIf_delete();
		MDataMap dataMap = new MDataMap();
		dataMap.put("purchase_order_id",purchase_order_id);
		dataMap.put("adress_id",adress_id );
		dataMap.put("select_flag", select_flag);
		dataMap.put("receiver",receiver );
		dataMap.put("province_city_district_code",province_city_district_code );
		dataMap.put("detail_addtess",detail_addtess );
		dataMap.put("postcode", postcode);
		dataMap.put("phone", phone);
		dataMap.put("identity_number",identity_number );
		dataMap.put("if_delete",if_delete );
		if("add".equals(operateName)) {
			dataMap.put("adress_id",WebHelper.upCode("ADR"));
			dataMap.put("uid",WebHelper.upUuid());
			//测试要求填写相同地址的要做去重处理。。。
			int count = DbUp.upTable("oc_purchase_order_address").count("receiver",receiver,"province_city_district_code",province_city_district_code,"detail_addtess",detail_addtess,"phone",phone,"if_delete","1");
			if(count==0) {
				DbUp.upTable("oc_purchase_order_address").dataInsert(dataMap);
			}
		}else if("edit".equals(operateName)) {
			int count = DbUp.upTable("oc_purchase_order_address").count("receiver",receiver,"province_city_district_code",province_city_district_code,"detail_addtess",detail_addtess,"phone",phone,"if_delete","1");
			if(count==0) {
				DbUp.upTable("oc_purchase_order_address").dataUpdate(dataMap, "select_flag,receiver,province_city_district_code,detail_addtess,postcode,phone,identity_number,if_delete", "adress_id");
			}
		}else if("delete".equals(operateName)) {
			DbUp.upTable("oc_purchase_order_address").dataUpdate(dataMap, "if_delete", "adress_id");
		}
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_purchase_order_address").dataSqlList("select * from oc_purchase_order_address where  if_delete='1' order by zid desc ",null);
		Map<String, Object> dataSqlOne = DbUp.upTable("oc_purchase_order").dataSqlOne("select * from oc_purchase_order where purchase_order_id=:purchase_order_id", new MDataMap("purchase_order_id",purchase_order_id));
		String selectAddressId = "";
		if(dataSqlOne!=null) {
			selectAddressId = dataSqlOne.get("adress_id").toString();
		}
		List<AddressDetail> paramList= new ArrayList<AddressDetail>();
		for (Map<String, Object> map : dataSqlList) {
        	AddressDetail addressDetail = new AddressDetail();
        	addressDetail.setAdress_id(map.get("adress_id").toString());
        	addressDetail.setDetail_addtess(map.get("detail_addtess").toString());
        	addressDetail.setIdentity_number(map.get("identity_number").toString());
        	addressDetail.setPhone(map.get("phone").toString());
        	addressDetail.setPostcode(map.get("postcode").toString());
        	addressDetail.setProvince_city_district_code(map.get("province_city_district_code").toString());
        	addressDetail.setPurchase_order_id(map.get("purchase_order_id").toString());
        	addressDetail.setReceiver(map.get("receiver").toString());
        	if(selectAddressId.equals(map.get("adress_id").toString())) {
            	addressDetail.setSelect_flag("1");
        	}else {
            	addressDetail.setSelect_flag("0");
        	}
        	String pcdc = map.get("province_city_district_code").toString();
			String[] split = pcdc.split("_");
			String pcdv = "";
			for (String code : split) {
				MDataMap one = DbUp.upTable("sc_tmp").one("code",code);
				pcdv=pcdv+one.get("name");
			}
			addressDetail.setPcdv(pcdv);
			paramList.add(addressDetail);
		}
		//选中的放在第一位
		Collections.sort(paramList, new Comparator<AddressDetail>() {
			@Override
			public int compare(AddressDetail o1, AddressDetail o2) {
				String detail1 = o1.getSelect_flag();
				String detail2 = o2.getSelect_flag();
				return detail2.compareTo(detail1);
			}
		});
		result.setAddressDetailList(paramList);
		return result;
	}



}
