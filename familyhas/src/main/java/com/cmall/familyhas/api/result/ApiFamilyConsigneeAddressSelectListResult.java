package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiFamilyConsigneeAddressSelectListResult {
	
	@ZapcomApi(value="收获地址编号")
	private String address_id="";
	@ZapcomApi(value="收货人")
	private String receive_person="";
	
	@ZapcomApi(value="省编码")
	private String prov_code="";
	@ZapcomApi(value="市编码")
	private String city_code="";
	@ZapcomApi(value="街道编码")
	private String street_code;
	@ZapcomApi(value="地区编码")
	private String area_code="";
	@ZapcomApi(value="省名称")
	private String prov_name="";
	@ZapcomApi(value="市名称")
	private String city_name="";
	@ZapcomApi(value="地区名称")
	private String area_name="";
	@ZapcomApi(value="街道名称")
	private String street_name;
	
	@ZapcomApi(value="详细地址")
	private String address="";
	@ZapcomApi(value="邮政编码")
	private String postcode="";
	@ZapcomApi(value="电话")
	private String mobilephone="";
	@ZapcomApi(value="标记",remark="1:默认 0不默认")
	private String flag_default="";
	
	@ZapcomApi(value="价钱",remark="相当于运费")
	private String price="";
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFlag_default() {
		return flag_default;
	}
	public void setFlag_default(String flag_default) {
		this.flag_default = flag_default;
	}
	public String getReceive_person() {
		return receive_person;
	}
	public void setReceive_person(String receive_person) {
		this.receive_person = receive_person;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getProv_code() {
		return prov_code;
	}
	public void setProv_code(String prov_code) {
		this.prov_code = prov_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getProv_name() {
		return prov_name;
	}
	public void setProv_name(String prov_name) {
		this.prov_name = prov_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getStreet_code() {
		return street_code;
	}
	public void setStreet_code(String street_code) {
		this.street_code = street_code;
	}
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
}
