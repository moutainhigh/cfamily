package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForValidateWeChatBoundInput extends RootInput{

	@ZapcomApi(value="唯一码",require=1)
	private String uid ="";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
    

}
