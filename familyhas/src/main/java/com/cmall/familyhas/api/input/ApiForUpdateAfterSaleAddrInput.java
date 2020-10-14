package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForUpdateAfterSaleAddrInput extends RootInput {

	@ZapcomApi(value = "uid",require=1)
	private String uid = "" ;
	
	@ZapcomApi(value = "售后单编号",require=1)
	private String asale_code = "" ;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAsale_code() {
		return asale_code;
	}

	public void setAsale_code(String asale_code) {
		this.asale_code = asale_code;
	}

}
