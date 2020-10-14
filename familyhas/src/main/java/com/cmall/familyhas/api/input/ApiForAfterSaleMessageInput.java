package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAfterSaleMessageInput extends RootInput {


	@ZapcomApi(value = "售后类型", require = 1)
	private String type = "";
	
	@ZapcomApi(value = "flag",require=1)
	private String flag = "" ;
	
	@ZapcomApi(value = "售后单编号",require=1)
	private String asale_code = "" ;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAsale_code() {
		return asale_code;
	}

	public void setAsale_code(String asale_code) {
		this.asale_code = asale_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
