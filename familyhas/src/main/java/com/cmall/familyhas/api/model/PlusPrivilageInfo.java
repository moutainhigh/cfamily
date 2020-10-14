package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
 * 橙意卡会员权益实体类
* @author Angel Joy
* @Time 2020年6月28日 下午3:05:33 
* @Version 1.0
* <p>Description:</p>
*/
public class PlusPrivilageInfo {

	@ZapcomApi(value="权益图片",remark="")
	private String privilegeUrl = "";
	
	@ZapcomApi(value="权益名称",remark="")
	private String privilegeName = "";
	
	@ZapcomApi(value="权益说明",remark="")
	private String privilegeDesc = "";
	
	@ZapcomApi(value="权益类型",remark="1：商品，2：电话，3：url，4：拓展待定")
	private String privilegeType = "";
	
	@ZapcomApi(value="权益链接",remark="如果是电话，此值放电话号码，如果是商品，此值是商品编号")
	private String privilegeValue = "";

	public String getPrivilegeUrl() {
		return privilegeUrl;
	}

	public void setPrivilegeUrl(String privilegeUrl) {
		this.privilegeUrl = privilegeUrl;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public String getPrivilegeDesc() {
		return privilegeDesc;
	}

	public void setPrivilegeDesc(String privilegeDesc) {
		this.privilegeDesc = privilegeDesc;
	}

	public String getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(String privilegeType) {
		this.privilegeType = privilegeType;
	}

	public String getPrivilegeValue() {
		return privilegeValue;
	}

	public void setPrivilegeValue(String privilegeValue) {
		this.privilegeValue = privilegeValue;
	}
	
	
}
