package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetDLQInfoInput extends RootInput { 
	
	@ZapcomApi(value="电视台编号",require=1)
	private String tv_number = "";

	@ZapcomApi(value="渠道",require=1)
	private String p_type = "";
	
	public String getTv_number() {
		return tv_number;
	}

	public void setTv_number(String tv_number) {
		this.tv_number = tv_number;
	}

	public String getP_type() {
		return p_type;
	}

	public void setP_type(String p_type) {
		this.p_type = p_type;
	}
	
	

}
