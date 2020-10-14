package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiFarmWateringInput extends RootInput{

	@ZapcomApi(value = "活动编号", require = 1)
	private String eventCode = "";
	@ZapcomApi(value = "果树编号", require = 1)
	private String treeCode = "";
	@ZapcomApi(value = "被浇水人用户编号", remark = "如果是用户给别人浇水，此字段必须有值")
	private String byMemberCode = "";

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public String getByMemberCode() {
		return byMemberCode;
	}

	public void setByMemberCode(String byMemberCode) {
		this.byMemberCode = byMemberCode;
	}
	
}
