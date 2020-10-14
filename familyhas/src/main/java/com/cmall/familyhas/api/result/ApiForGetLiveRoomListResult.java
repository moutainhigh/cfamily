package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.LiveRoom;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetLiveRoomListResult extends RootResult {

	@ZapcomApi(value="分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value = "直播房间列表")
	private List<LiveRoom> liveRoomList = new ArrayList<LiveRoom>();

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<LiveRoom> getLiveRoomList() {
		return liveRoomList;
	}

	public void setLiveRoomList(List<LiveRoom> liveRoomList) {
		this.liveRoomList = liveRoomList;
	}
	
	
}
