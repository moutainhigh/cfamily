package com.cmall.familyhas.api.result;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForValidateWeChatBoundResult extends RootResultWeb{

	@ZapcomApi(value="领券码")
	private String validate_code = "";
	@ZapcomApi(value="电话号码")
	private String phone = "";
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getValidate_code() {
		return validate_code;
	}

	public void setValidate_code(String validate_code) {
		this.validate_code = validate_code;
	}
	
	
	
	

}