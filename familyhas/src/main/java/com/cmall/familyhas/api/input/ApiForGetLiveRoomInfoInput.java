package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetLiveRoomInfoInput extends RootInput {

	@ZapcomApi(value = "主播还是用户", remark = "0:主播 ; 1:用户", require = 1)
	private String anchorOrUser = "";
	
	@ZapcomApi(value = "直播房间编号", remark = "")
	private String liveRoomId = "";
	
	@ZapcomApi(value = "回放文件编号", remark = "直播间回放专用,用于区分多个回放文件的直播")
	private String fileId = "";

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getAnchorOrUser() {
		return anchorOrUser;
	}

	public void setAnchorOrUser(String anchorOrUser) {
		this.anchorOrUser = anchorOrUser;
	}

	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	
}
