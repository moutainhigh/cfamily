package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCutCakeSaveBlessingInput extends RootInput {

	@ZapcomApi(value="用户的切蛋糕祝福语", remark = "")
	private String cakeBlessing = "";

	public String getCakeBlessing() {
		return cakeBlessing;
	}

	public void setCakeBlessing(String cakeBlessing) {
		this.cakeBlessing = cakeBlessing;
	}
	
	
}
