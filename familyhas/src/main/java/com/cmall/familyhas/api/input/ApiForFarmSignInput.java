package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFarmSignInput extends RootInput {

	@ZapcomApi(value = "链接类型", remark = "1: 进入签到列表 ; 2: 点击签到", require = 1)
	private String linkType = "1";
	
	@ZapcomApi(value = "签到编号", remark = "linkType为'2: 点击签到'时传入")
	private String signCode = "";

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	
}
