package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiBeachDelProdForPreResult extends RootResult {

	@ZapcomApi(value = "分类编号", remark = "", require = 1)
	private String nodeId = "";

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
