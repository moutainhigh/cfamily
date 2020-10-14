package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * MT管家返回结果实体
 * 
 * @author zhanghs
 *
 */
public class MTUserRegisterApiResult extends RootResultWeb{
	
	@ZapcomApi(value = "用户编号",demo="MI140000100001")
	private String memberCode = "";

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
}
