package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiTencentDropLiveStreamInput extends RootInput {
	@ZapcomApi(value = "房间编号", remark = "不可为空", demo = "131833",require=1)
	private String live_room_id;

	public String getLive_room_id() {
		return live_room_id;
	}

	public void setLive_room_id(String live_room_id) {
		this.live_room_id = live_room_id;
	}
	
}
