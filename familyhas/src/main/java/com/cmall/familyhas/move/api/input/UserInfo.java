package com.cmall.familyhas.move.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class UserInfo extends RootInput {

	@ZapcomApi(value = "登录名", require = 1,remark="用户的手机号")
	private String loginName = "";
	@ZapcomApi(value = "登录密码",remark="用户的密码，没有可以为空")
	private String loginPassword = "";
	@ZapcomApi(value = "旧系统用户编号", require = 1,remark="用户的编号，YT_Vipuser_Num")
	private String oldCode = "";
	@ZapcomApi(value = "家有用户编号",remark="家有的用户编号")
	private String homehasCode = "";

	@ZapcomApi(value = "用户标记", require = 1, verify = { "in=4497467900030001,4497467900030002" }, remark = { "用户标记，区分普通用户和小件员，其中4497467900030001表示普通用户，4497467900030002表示小件员用户" })
	private String memberSign = "";

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public String getHomehasCode() {
		return homehasCode;
	}

	public void setHomehasCode(String homehasCode) {
		this.homehasCode = homehasCode;
	}

	public String getMemberSign() {
		return memberSign;
	}

	public void setMemberSign(String memberSign) {
		this.memberSign = memberSign;
	}

}
