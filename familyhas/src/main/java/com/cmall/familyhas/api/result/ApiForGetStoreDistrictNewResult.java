package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetStoreDistrictNewResult extends RootResultWeb {
	@ZapcomApi(value = "地区列表", remark = "库存地区列表")
	private  List<Area> areaList = new ArrayList<Area>();
	
	public static class Area {
		@ZapcomApi(value="区域ID")
		private String areaId = "";
		@ZapcomApi(value="区域名称")
		private String areaName = "";
		@ZapcomApi(value="区域等级")
		private String codeLvl = "";
		@ZapcomApi(value="是否显示", remark="Y：显示，N：不显示")
		private String isShow = "";
		
		public String getAreaId() {
			return areaId;
		}
		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}
		
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		
		public String getCodeLvl() {
			return codeLvl;
		}
		public void setCodeLvl(String codeLvl) {
			this.codeLvl = codeLvl;
		}
		
		public String getIsShow() {
			return isShow;
		}
		public void setIsShow(String isShow) {
			this.isShow = isShow;
		}
	}

	public List<Area> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}
}
