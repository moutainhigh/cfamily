package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetNeigouAddressResult extends RootResultWeb {
	@ZapcomApi(value = "一级编码", remark = "")
	private String province = "";
	@ZapcomApi(value = "二级编码", remark = "")
	private String city = "";
	@ZapcomApi(value = "三级编码", remark = "")
	private String area = "";
	@ZapcomApi(value = "四级编码", remark = "")
	private String street = "";
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	
}
