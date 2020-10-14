package com.cmall.familyhas.api.input;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeColumnWapInput extends RootInput {
	
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "";

	@ZapcomApi(value="当前时间",demo="2015-03-10 22:22:22")
	private String sysDateTime = DateUtil.getSysDateTimeString();

	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	public String getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	public String getSysDateTime() {
		return sysDateTime;
	}

	public void setSysDateTime(String sysDateTime) {
		this.sysDateTime = sysDateTime;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}
	
	
}
