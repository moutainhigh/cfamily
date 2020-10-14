package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForPurchaseAddressInput extends RootInput{

	private String purchase_order_id ="";
	private String adress_id ="";
	private String select_flag ="";
	private String receiver ="";
	private String province_city_district_code ="";
	private String detail_addtess ="";
	private String postcode ="";
	private String phone ="";
	private String identity_number ="";
	@ZapcomApi(value="add,edit,delete")
	private String operateName = "";
	private String if_delete ="";

	public String getIf_delete() {
		return if_delete;
	}
	public void setIf_delete(String if_delete) {
		this.if_delete = if_delete;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIdentity_number() {
		return identity_number;
	}
	public void setIdentity_number(String identity_number) {
		this.identity_number = identity_number;
	}
	
	


}
