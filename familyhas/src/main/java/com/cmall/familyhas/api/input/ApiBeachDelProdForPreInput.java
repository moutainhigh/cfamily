package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiBeachDelProdForPreInput extends RootInput {

	@ZapcomApi(value = "批量删除商品uid字符串", remark = "", require = 1)
	private String delProdUids = "";
	
	@ZapcomApi(value = "分类编号", remark = "", require = 1)
	private String nodeId = "";

	public String getDelProdUids() {
		return delProdUids;
	}

	public void setDelProdUids(String delProdUids) {
		this.delProdUids = delProdUids;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
