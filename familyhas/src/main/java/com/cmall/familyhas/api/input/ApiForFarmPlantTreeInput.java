package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFarmPlantTreeInput extends RootInput {

	@ZapcomApi(value = "种植的果树类型")
	private String treeType = "";

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	
}
