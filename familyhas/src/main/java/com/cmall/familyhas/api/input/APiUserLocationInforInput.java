package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiUserLocationInforInput extends RootInput {


	@ZapcomApi(value = "操作流水号", remark="操作流水号" ,demo="LSH0001")
	private String sqNum = "";
	
	@ZapcomApi(value = "城市", remark="城市" ,demo="北京")
	private String cityName = "";
	
	@ZapcomApi(value = "经度", remark="经度" ,demo="")
	private String longitude = "";
	
	@ZapcomApi(value = "纬度", remark="纬度" ,demo="")
	private String latitude = "";

	public String getSqNum() {
		return sqNum;
	}

	public void setSqNum(String sqNum) {
		this.sqNum = sqNum;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	
}
