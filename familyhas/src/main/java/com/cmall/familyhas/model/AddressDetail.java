package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	采购信息
*   zhangbo
*/
public class AddressDetail  {
	@ZapcomApi(value = "采购单编号")
	private String purchase_order_id = "";
	
	@ZapcomApi(value = "地址编码")
	private String adress_id = "";
	
	@ZapcomApi(value = "是否选中默认地址")
	private String select_flag = "";
	
	@ZapcomApi(value = "收货人姓名")
	private String receiver = "";
	
	@ZapcomApi(value = "省市区编码")
	private String province_city_district_code = "";
	
	@ZapcomApi(value = "详细地址")
	private String detail_addtess = "";
	
	@ZapcomApi(value = "地区编码")
	private String postcode = "";
	
	@ZapcomApi(value = "身份证号码")
	private String identity_number = "";
	
	@ZapcomApi(value = "电话")
	private String phone = "";
	
	private String pcdv = "";

	
	public String getPcdv() {
		return pcdv;
	}

	public void setPcdv(String pcdv) {
		this.pcdv = pcdv;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPurchase_order_id() {
		return purchase_order_id;
	}

	public void setPurchase_order_id(String purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}

	public String getAdress_id() {
		return adress_id;
	}

	public void setAdress_id(String adress_id) {
		this.adress_id = adress_id;
	}

	public String getSelect_flag() {
		return select_flag;
	}

	public void setSelect_flag(String select_flag) {
		this.select_flag = select_flag;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getProvince_city_district_code() {
		return province_city_district_code;
	}

	public void setProvince_city_district_code(String province_city_district_code) {
		this.province_city_district_code = province_city_district_code;
	}

	public String getDetail_addtess() {
		return detail_addtess;
	}

	public void setDetail_addtess(String detail_addtess) {
		this.detail_addtess = detail_addtess;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getIdentity_number() {
		return identity_number;
	}

	public void setIdentity_number(String identity_number) {
		this.identity_number = identity_number;
	}
	
	
	

	
	

}

