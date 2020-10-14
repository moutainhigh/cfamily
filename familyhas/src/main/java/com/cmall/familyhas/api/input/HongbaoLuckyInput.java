package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class HongbaoLuckyInput extends RootInput{
	
	@ZapcomApi(value="操作人手机号",remark="操作人手机号")
	private String phone = "";//
	
	@ZapcomApi(value="请求时间",remark="格式为yyyyMMddHHmmssffff")
	private String reqTime = "";//
	
	private String secKey = "rUoaeUtGmxJQNzLcY3eOsbByYKk7zsoA";//
	
	@ZapcomApi(value="加密后字符串",remark="加密后字符串")
	private String resultStr = "";//

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}
	
	
	
}