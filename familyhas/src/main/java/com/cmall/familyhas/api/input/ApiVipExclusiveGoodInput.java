package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiVipExclusiveGoodInput extends RootInput {
	
	@ZapcomApi(value="客户积分")
	private String custPoint = "0";
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "0";
	@ZapcomApi(value="图片格式")
	private String picType = "";
	
	public String getCustPoint() {
		return custPoint;
	}
	public void setCustPoint(String custPoint) {
		this.custPoint = custPoint;
	}
	
	public String getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
}
