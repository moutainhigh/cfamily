package com.cmall.familyhas.api.input;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeColumnInput extends RootInput {

	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "";

	@ZapcomApi(value="图片格式",remark="jpg")
	private String picType = "";

	@ZapcomApi(value="当前时间",demo="2015-03-10 22:22:22")
	private String sysDateTime = DateUtil.getSysDateTimeString();

	@ZapcomApi(value = "用户类型", remark="4497469400050002" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value = "展示平台", remark="默认为app首页" ,demo="4497471600100001:APP首页，4497471600100002:Wap商城首页，4497471600100003:Web商城首页")
	private String viewType = "4497471600100001";
	
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

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getPicType() {
		return picType;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}
	
}
