package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForLiveRoomOperateInput extends RootInput {

	@ZapcomApi(value = "直播房间编号", remark = "",require=1)
	private String liveRoomId = "";
	
	@ZapcomApi(value = "直播间操作类型", remark = "449748630001	观看;449748630002		点赞;449748630003		评论",require=1)
	private String behaviorType = "";

	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public String getBehaviorType() {
		return behaviorType;
	}

	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}
	
}
