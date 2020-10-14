package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FarmEvent;
import com.cmall.familyhas.api.model.FarmTree;
import com.cmall.familyhas.api.model.FarmWater;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmHomeResult extends RootResult {

	@ZapcomApi(value = "活动信息")
	private FarmEvent farmEvent = new FarmEvent();
	
	@ZapcomApi(value = "果树信息")
	private FarmTree farmTree = new FarmTree();
	
	@ZapcomApi(value = "水滴信息",remark = "多个水滴")
	private List<FarmWater> list = new ArrayList<FarmWater>();
	
	@ZapcomApi(value = "水壶剩余总量",remark = "去到别人农场首页时用到")
	private int kettleWater = 0;
	
	@ZapcomApi(value = "水壶编号")
	private String kettle_code = "";
	
	@ZapcomApi(value = "别人的用户昵称或者手机号加密",remark = "去到别人农场时返回")
	private String othersNickName = "";

	public String getKettle_code() {
		return kettle_code;
	}

	public void setKettle_code(String kettle_code) {
		this.kettle_code = kettle_code;
	}

	public String getOthersNickName() {
		return othersNickName;
	}

	public void setOthersNickName(String othersNickName) {
		this.othersNickName = othersNickName;
	}

	public FarmEvent getFarmEvent() {
		return farmEvent;
	}

	public void setFarmEvent(FarmEvent farmEvent) {
		this.farmEvent = farmEvent;
	}

	public FarmTree getFarmTree() {
		return farmTree;
	}

	public void setFarmTree(FarmTree farmTree) {
		this.farmTree = farmTree;
	}

	public List<FarmWater> getList() {
		return list;
	}

	public void setList(List<FarmWater> list) {
		this.list = list;
	}

	public int getKettleWater() {
		return kettleWater;
	}

	public void setKettleWater(int kettleWater) {
		this.kettleWater = kettleWater;
	}
	
	
}
