package com.cmall.familyhas.api.input.apphome;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;


public class AppHomeActivityInput extends RootInput{
	
	@ZapcomApi(value="活动UID",remark="活动UID，用以获取活动详细信息",require=1)
	String uid = "";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
}
