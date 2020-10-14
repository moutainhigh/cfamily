package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetLiveInfoResult extends RootResultWeb {
	@ZapcomApi(value = "直播房间信息",remark = "直播房间信息")
	private List<ApiRoomInfoResult> roomInfo = new ArrayList<ApiRoomInfoResult>();
	@ZapcomApi(value = "直播房间数量",remark = "直播房间数量")
	private int total = 0;
	@ZapcomApi(value = "总页数",remark = "总页数")
	private int totalPage = 0;

	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<ApiRoomInfoResult> getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(List<ApiRoomInfoResult> roomInfo) {
		this.roomInfo = roomInfo;
	}
	
}
