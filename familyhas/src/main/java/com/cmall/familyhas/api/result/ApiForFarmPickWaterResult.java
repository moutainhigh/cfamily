package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmPickWaterResult extends RootResult {

	@ZapcomApi(value = "水滴编号")
	private String waterCode = "";
	
	@ZapcomApi(value = "水滴剩余量")
	private int waterNum = 0;
	
	@ZapcomApi(value = "水壶总量")
	private int kettleWater = 0;
	
	@ZapcomApi(value = "偷取水滴量")
	private int stealWaterNum = 0;

	public int getStealWaterNum() {
		return stealWaterNum;
	}

	public void setStealWaterNum(int stealWaterNum) {
		this.stealWaterNum = stealWaterNum;
	}

	public String getWaterCode() {
		return waterCode;
	}

	public void setWaterCode(String waterCode) {
		this.waterCode = waterCode;
	}

	public int getWaterNum() {
		return waterNum;
	}

	public void setWaterNum(int waterNum) {
		this.waterNum = waterNum;
	}

	public int getKettleWater() {
		return kettleWater;
	}

	public void setKettleWater(int kettleWater) {
		this.kettleWater = kettleWater;
	}
	
}
