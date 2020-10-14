package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiLiveRoomProductsOperateInput extends RootInput{

	private String liveRoomId = "";
	private String pCode = "";
	private String doFlag = "";
	public String getLiveRoomId() {
		return liveRoomId;
	}
	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getDoFlag() {
		return doFlag;
	}
	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}

	
	
	
	
	
}
