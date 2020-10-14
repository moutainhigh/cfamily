package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHbWithdrawInput extends RootInput {
	
	@ZapcomApi(value="姓名",require=1)
	private String name="";
	
	@ZapcomApi(value="身份证号",require=1)
	private String idcard_number = "";
	
	@ZapcomApi(value="验证码唯一编号")
	private String verification_code = "";
	
	@ZapcomApi(value="验证码",require=1)
	private String verification_info = "";
	
	@ZapcomApi(value="提现金额",require = 1)
	private String apply_money = "";
	
	@ZapcomApi(value="用户openid",require=1)
	private String openid = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard_number() {
		return idcard_number;
	}

	public void setIdcard_number(String idcard_number) {
		this.idcard_number = idcard_number;
	}

	public String getVerification_code() {
		return verification_code;
	}

	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}

	public String getVerification_info() {
		return verification_info;
	}

	public void setVerification_info(String verification_info) {
		this.verification_info = verification_info;
	}

	public String getApply_money() {
		return apply_money;
	}

	public void setApply_money(String apply_money) {
		this.apply_money = apply_money;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	
}
