package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiFarmWateringResult extends RootResultWeb{

	@ZapcomApi(value = "提示语", demo = "再浇100%的雨露核桃树包邮到家")
	private String message = "";
	@ZapcomApi(value = "下一果树阶段")
	private String treeStage = "";
	@ZapcomApi(value = "水壶中剩余水量")
	private int kettleWater = 0;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTreeStage() {
		return treeStage;
	}
	public void setTreeStage(String treeStage) {
		this.treeStage = treeStage;
	}
	public int getKettleWater() {
		return kettleWater;
	}
	public void setKettleWater(int kettleWater) {
		this.kettleWater = kettleWater;
	}
	
}
