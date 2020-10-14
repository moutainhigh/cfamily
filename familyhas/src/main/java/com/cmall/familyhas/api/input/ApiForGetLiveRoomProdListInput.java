package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetLiveRoomProdListInput extends RootInput {

	@ZapcomApi(value = "直播间编号", remark = "",require=1)
	private String liveRoomId = "";
	
	@ZapcomApi(value="分页页码",remark="第几页数据",require=1)
	private int page = 1;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	
}
