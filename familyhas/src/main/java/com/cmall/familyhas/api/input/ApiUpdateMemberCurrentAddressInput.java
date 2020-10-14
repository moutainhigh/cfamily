package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 
 * @author huangs
 * @describe 更新用户当前地址请求参数
 *
 */
public class ApiUpdateMemberCurrentAddressInput extends RootInput {
	@ZapcomApi(value = "城市")
	public String city="";
	@ZapcomApi(value = "经度")
	public double longitude=0.0d;
	@ZapcomApi(value = "维度")
	public double latitude=0.0d;
	@ZapcomApi(value = "流水号",demo="LSH0001")
	public String sqNum="";
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getSqNum() {
		return sqNum;
	}
	public void setSqNum(String sqNum) {
		this.sqNum = sqNum;
	}
	
	
	
	
	
}
