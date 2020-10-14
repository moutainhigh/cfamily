package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetFxrResult extends RootResult{
	
	@ZapcomApi(value = "用户编码",remark="")
	private String memberCode;
	
	@ZapcomApi(value = "手机号",remark="")
	private String mobile;
	
	@ZapcomApi(value = "昵称",remark="")
	private String nickName;

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
	
}
