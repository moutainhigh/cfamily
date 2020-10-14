package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/***
 * 查询仓库覆盖的区域 输出参数
 * @author jlin
 *
 */
public class ApiForGetStoreDistrictResult extends RootResultWeb {

	@ZapcomApi(value = "地区列表", remark = "库存地区列表")
	private  List<Province> list = new ArrayList<Province>();
	
	/**
	 * 库存地区
	 * @author jlin
	 *
	 */
	public static class StoreSite {
		@ZapcomApi(value = "库存ID")
		private String stateID = "";
		@ZapcomApi(value = "库存名称")
		private String stateName = "";
		@ZapcomApi(value = "省列表")
		private List<ApiForGetStoreDistrictResult.Province> provinceList = new ArrayList<ApiForGetStoreDistrictResult.Province>();
		public String getStateID() {
			return stateID;
		}
		public void setStateID(String stateID) {
			this.stateID = stateID;
		}
		public String getStateName() {
			return stateName;
		}
		public void setStateName(String stateName) {
			this.stateName = stateName;
		}
		public List<ApiForGetStoreDistrictResult.Province> getProvinceList() {
			return provinceList;
		}
		public void setProvinceList(
				List<ApiForGetStoreDistrictResult.Province> provinceList) {
			this.provinceList = provinceList;
		}
	}
	
	/**
	 * 省
	 * @author jlin
	 *
	 */
	public static class Province {
		@ZapcomApi(value = "省ID")
		private String provinceID = "";
		@ZapcomApi(value = "省名称")
		private String provinceName = "";
		@ZapcomApi(value = "城市列表")
		private List<ApiForGetStoreDistrictResult.City> cityList = new ArrayList<ApiForGetStoreDistrictResult.City>();
		public List<ApiForGetStoreDistrictResult.City> getCityList() {
			return cityList;
		}
		public void setCityList(List<ApiForGetStoreDistrictResult.City> cityList) {
			this.cityList = cityList;
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
	
	
	/***
	 * 城市
	 * @author jlin
	 *
	 */
	public static class City {
		
		@ZapcomApi(value = "城市ID")
		private String cityID = "";
		@ZapcomApi(value = "城市名称")
		private String cityName = "";
		@ZapcomApi(value = "区域列表")
		private List<ApiForGetStoreDistrictResult.District> districtList=new ArrayList<ApiForGetStoreDistrictResult.District>();
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
		public List<ApiForGetStoreDistrictResult.District> getDistrictList() {
			return districtList;
		}
		public void setDistrictList(
				List<ApiForGetStoreDistrictResult.District> districtList) {
			this.districtList = districtList;
		}
		
	}
	
	/***
	 * 区域
	 * @author jlin
	 *
	 */
	public static class District {
		
		@ZapcomApi(value = "区域名称")
		private String district = "";
		@ZapcomApi(value = "区域ID")
		private String districtID = "";

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public String getDistrictID() {
			return districtID;
		}

		public void setDistrictID(String districtID) {
			this.districtID = districtID;
		}
	}

	public List<Province> getList() {
		return list;
	}

	public void setList(List<Province> list) {
		this.list = list;
	}
	
	
}
