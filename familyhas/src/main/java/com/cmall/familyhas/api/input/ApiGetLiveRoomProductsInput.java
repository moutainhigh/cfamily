package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetLiveRoomProductsInput extends RootInput{

	private String liveRoomId = "";
	private String pageNum = "";
	private String productName = "";
	private String productCode = "";
	public String getLiveRoomId() {
		return liveRoomId;
	}
	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
	
	
}
