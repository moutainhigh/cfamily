package com.cmall.familyhas.api.input;


import com.srnpr.zapcom.topapi.RootInput;

public class ApiChangeCommentOrReplayStatusInput extends RootInput{


	private String flag = "";

	private String liveRoomId="";
	
	
	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	


	
}
