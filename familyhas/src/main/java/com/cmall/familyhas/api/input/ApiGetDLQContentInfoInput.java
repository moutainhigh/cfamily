package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetDLQContentInfoInput  extends RootInput { 
	
	
	@ZapcomApi(value="页面编号",require=1)
	private String page_number = "";
	
	@ZapcomApi(value="电视台编号",require=1)
	private String tv_number = "";
	
	@ZapcomApi(value="渠道类型",require=1)
	private String p_type = "";
	
	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

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
