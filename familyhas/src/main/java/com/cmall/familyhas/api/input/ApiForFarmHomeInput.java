package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFarmHomeInput extends RootInput {

	@ZapcomApi(value = "别人用户编号",remark = "去到别人农场首页时用到")
	private String othersMemberCode="";

	public String getOthersMemberCode() {
		return othersMemberCode;
	}

	public void setOthersMemberCode(String othersMemberCode) {
		this.othersMemberCode = othersMemberCode;
	}
	
	
}
