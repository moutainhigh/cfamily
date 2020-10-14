package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiBatchAddProductInput extends RootInput {

	@ZapcomApi(value = "属性编号", remark = "", require = 1)
	private String properties_code = "";
	
	@ZapcomApi(value = "分类编号", remark = "", require = 1)
	private String nodeId = "";
	
	@ZapcomApi(value = "上移/下移", remark = "", require = 1)
	private String type = "";

	public String getProperties_code() {
		return properties_code;
	}

	public void setProperties_code(String properties_code) {
		this.properties_code = properties_code;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
