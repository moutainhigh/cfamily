package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetQaInput extends RootInput {

	@ZapcomApi(value="问题编号",require=1)
	private String qaCode;

	public String getQaCode() {
		return qaCode;
	}

	public void setQaCode(String qaCode) {
		this.qaCode = qaCode;
	}

}
