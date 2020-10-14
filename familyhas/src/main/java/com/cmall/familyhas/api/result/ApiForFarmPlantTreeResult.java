package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmPlantTreeResult extends RootResult {

	@ZapcomApi(value = "果树种植是否成功", remark = "0否 ; 1是(种植成功调用农场首页接口)")
	private String plantFlag = "1";

	public String getPlantFlag() {
		return plantFlag;
	}

	public void setPlantFlag(String plantFlag) {
		this.plantFlag = plantFlag;
	}
	
	
}
