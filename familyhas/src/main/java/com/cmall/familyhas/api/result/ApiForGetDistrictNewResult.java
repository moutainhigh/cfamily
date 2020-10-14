package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetDistrictNewResult extends RootResultWeb {
	@ZapcomApi(value = "一级地区列表", remark = "库存地区列表")
	private  List<Area> level1AreaList = new ArrayList<Area>();
	@ZapcomApi(value = "二级地区列表", remark = "库存地区列表")
	private  List<Area> level2AreaList = new ArrayList<Area>();
	@ZapcomApi(value = "三级地区列表", remark = "库存地区列表")
	private  List<Area> level3AreaList = new ArrayList<Area>();
	@ZapcomApi(value = "四级地区列表", remark = "库存地区列表")
	private  List<Area> level4AreaList = new ArrayList<Area>();
	
	
	
	
	
	public List<Area> getLevel1AreaList() {
		return level1AreaList;
	}





	public void setLevel1AreaList(List<Area> level1AreaList) {
		this.level1AreaList = level1AreaList;
	}





	public List<Area> getLevel2AreaList() {
		return level2AreaList;
	}





	public void setLevel2AreaList(List<Area> level2AreaList) {
		this.level2AreaList = level2AreaList;
	}





	public List<Area> getLevel3AreaList() {
		return level3AreaList;
	}





	public void setLevel3AreaList(List<Area> level3AreaList) {
		this.level3AreaList = level3AreaList;
	}





	public List<Area> getLevel4AreaList() {
		return level4AreaList;
	}





	public void setLevel4AreaList(List<Area> level4AreaList) {
		this.level4AreaList = level4AreaList;
	}





	public static class Area {
		@ZapcomApi(value="区域ID")
		private String areaId = "";
		@ZapcomApi(value="区域名称")
		private String areaName = "";
		@ZapcomApi(value="区域等级")
		private String codeLvl = "";
		@ZapcomApi(value="是否显示", remark="Y：显示，N：不显示")
		private String isShow = "";
		@ZapcomApi(value="是否选中", remark="Y：是，N：否")
		private String isChoosed = "N";
		
		public String getIsChoosed() {
			return isChoosed;
		}
		public void setIsChoosed(String isChoosed) {
			this.isChoosed = isChoosed;
		}
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

}
