package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 
 * @author xiegj
 *	登录账户信息
 */
public class ApiForLoginAccountInput extends RootInput {
	
	@ZapcomApi(value = "用户名", remark = "用户名", demo = "13512345678", require = 1)
	private String loginName = "";

	@ZapcomApi(value = "用户密码", require = 1, demo = "123456", remark = "加密后的密码")
	private String loginPass = "";

	@ZapcomApi(value = "用户token", require = 1, demo = "123456", remark = "用户登录微公社的token串")
	private String loginToken = "";
	
	@ZapcomApi(value = "用户MI编号", require = 1, demo = "MI123456", remark = "")
	private String memberCode = "";
	
	@ZapcomApi(value = "用户AI编号", require = 1, demo = "AI123456", remark = "")
	private String accountCode = "";
	
	@ZapcomApi(value = "来源", require = 0, demo = "hjy_mem", remark = "用户token来源")
	private String projectCode = "";
	
	@ZapcomApi(value = "微信唯一编号", require = 0, remark = "")
	private String unionId = "";
	
	@ZapcomApi(value = "公众号的openid", require = 0, remark = "")
	private String openid_gzh = "";
	
	@ZapcomApi(value = "app的openid", require = 0, remark = "")
	private String openid_app = "";
	
	@ZapcomApi(value = "小程序的openid", require = 0, remark = "")
	private String openid_xch = "";
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPass() {
		return loginPass;
	}

	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getUnionId() {
		return unionId;
	}
	
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
	public String getOpenid_gzh() {
		return openid_gzh;
	}

	public void setOpenid_gzh(String openid_gzh) {
		this.openid_gzh = openid_gzh;
	}

	public String getOpenid_app() {
		return openid_app;
	}

	public void setOpenid_app(String openid_app) {
		this.openid_app = openid_app;
	}

	public String getOpenid_xch() {
		return openid_xch;
	}

	public void setOpenid_xch(String openid_xch) {
		this.openid_xch = openid_xch;
	}
	
}
