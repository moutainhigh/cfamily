package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForLiveRoomStartInput extends RootInput {

	@ZapcomApi(value = "直播房间编号", remark = "",require=1)
	private String liveRoomId = "";

	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	
	
}
