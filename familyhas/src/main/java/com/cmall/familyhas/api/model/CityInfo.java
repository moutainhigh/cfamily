package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 *  城市类
 * @author ligj
 * date 2016-1-04
 * @version 1.0
 */
public class CityInfo {
	
	@ZapcomApi(value="所属省id")
	private String provinceID  = "";
	
	@ZapcomApi(value="所属省名称")
	private String provinceName = "";

	@ZapcomApi(value="城市Id")
	private String cityID  = "";
	
	@ZapcomApi(value="城市名称")
	private String cityName = "";

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
}
