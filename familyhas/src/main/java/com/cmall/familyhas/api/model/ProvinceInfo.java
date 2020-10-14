package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 *  省类
 * @author ligj
 * date 2016-1-04
 * @version 1.0
 */
public class ProvinceInfo {

	@ZapcomApi(value="省id")
	private String provinceID  = "";
	
	@ZapcomApi(value="省名称")
	private String provinceName = "";

	@ZapcomApi(value="市信息")
	private List<CityInfo> cityList = new ArrayList<CityInfo>();

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

	public List<CityInfo> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityInfo> cityList) {
		this.cityList = cityList;
	}

}
