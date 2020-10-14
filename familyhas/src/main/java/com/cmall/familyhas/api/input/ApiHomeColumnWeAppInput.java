package com.cmall.familyhas.api.input;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeColumnWeAppInput extends RootInput {

	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "";

	@ZapcomApi(value="当前时间",demo="2015-03-10 22:22:22")
	private String sysDateTime = DateUtil.getSysDateTimeString();

	@ZapcomApi(value = "用户类型", remark="4497469400050002" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value="加载标识",demo="0: 全部，1： 首屏展示模版，2： 除首屏外剩余全部模版")
	private String loadFlag = "0";
	
	@ZapcomApi(value = "导航项编码", remark="编码从首页导航栏接口获取" ,demo="449716040037 推荐")
	private String navCode = "";
	
	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

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

	public String getLoadFlag() {
		return loadFlag;
	}

	public void setLoadFlag(String loadFlag) {
		this.loadFlag = loadFlag;
	}
	
}
