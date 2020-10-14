package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FarmWater {

	@ZapcomApi(value = "水滴编号")
	private String wateCode = "";
	
	@ZapcomApi(value = "水滴克数")
	private int waterNum = 0;
	
	@ZapcomApi(value = "是否可偷取",remark = " 0否; 1是")
	private String stealFlag = "0";

	public String getWateCode() {
		return wateCode;
	}

	public void setWateCode(String wateCode) {
		this.wateCode = wateCode;
	}

	public int getWaterNum() {
		return waterNum;
	}

	public void setWaterNum(int waterNum) {
		this.waterNum = waterNum;
	}

	public String getStealFlag() {
		return stealFlag;
	}

	public void setStealFlag(String stealFlag) {
		this.stealFlag = stealFlag;
	}
	
	
}
