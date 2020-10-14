package com.cmall.familyhas.api.input;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeColumnNewInput extends RootInput {

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
	
	@ZapcomApi(value = "导航项编码", remark="编码从首页导航栏接口获取" ,demo="449716040037 推荐")
	private String navCode = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "预览的导航项编码", remark="预览专用" )
	private String yuLan_navCode = "";
	
	@ZapcomApi(value = "时间节点", remark="预览专用" )
	private String yuLan_time_point = "";
	
	@ZapcomApi(value = "栏目名称", remark="预览专用" )
	private String yuLan_column_name = "";
	
	@ZapcomApi(value = "栏目类型", remark="预览专用" )
	private String yuLan_column_type = "";
	
	
	public String getYuLan_navCode() {
		return yuLan_navCode;
	}

	public void setYuLan_navCode(String yuLan_navCode) {
		this.yuLan_navCode = yuLan_navCode;
	}

	public String getYuLan_time_point() {
		return yuLan_time_point;
	}

	public void setYuLan_time_point(String yuLan_time_point) {
		this.yuLan_time_point = yuLan_time_point;
	}

	public String getYuLan_column_name() {
		return yuLan_column_name;
	}

	public void setYuLan_column_name(String yuLan_column_name) {
		this.yuLan_column_name = yuLan_column_name;
	}

	public String getYuLan_column_type() {
		return yuLan_column_type;
	}

	public void setYuLan_column_type(String yuLan_column_type) {
		this.yuLan_column_type = yuLan_column_type;
	}

	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
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

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	
}
