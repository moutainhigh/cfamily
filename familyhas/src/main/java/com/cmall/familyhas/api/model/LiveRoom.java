package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class LiveRoom {

	@ZapcomApi(value = "直播房间编号", remark = "")
	private String liveRoomId = "";
	
	@ZapcomApi(value = "房间名称", remark = "")
	private String liveTitle = "";
	
	@ZapcomApi(value = "房间状态", remark = "未开始(预告):449746320001 / 已结束(回放):449746320002 / 直播中:449746320003")
	private String liveStatus = "";
	
	@ZapcomApi(value = "观看总数", remark = "")
	private int lookNum = 0;
	
	@ZapcomApi(value = "预计开播时间", remark = "")
	private String startTime = "";
	
	@ZapcomApi(value = "直播封面图", remark = "")
	private String liveCoverPicture = "";
	
	@ZapcomApi(value = "直播商品总数", remark = "")
	private int liveRoomProdNum = 0;
	
	@ZapcomApi(value = "回放文件编号", remark = "直播间回放专用,用于区分多个回放文件的直播")
	private String fileId = "";
	
	@ZapcomApi(value = "回放文件地址", remark = "")
	private String videoUrl = "";
	
	@ZapcomApi(value = "直播商品集合", remark = "")
	private List<LiveProd> liveProdList = new ArrayList<LiveProd>();

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public int getLiveRoomProdNum() {
		return liveRoomProdNum;
	}

	public void setLiveRoomProdNum(int liveRoomProdNum) {
		this.liveRoomProdNum = liveRoomProdNum;
	}

	public String getLiveCoverPicture() {
		return liveCoverPicture;
	}

	public void setLiveCoverPicture(String liveCoverPicture) {
		this.liveCoverPicture = liveCoverPicture;
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public int getLookNum() {
		return lookNum;
	}

	public void setLookNum(int lookNum) {
		this.lookNum = lookNum;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public List<LiveProd> getLiveProdList() {
		return liveProdList;
	}

	public void setLiveProdList(List<LiveProd> liveProdList) {
		this.liveProdList = liveProdList;
	}
	
	
}
