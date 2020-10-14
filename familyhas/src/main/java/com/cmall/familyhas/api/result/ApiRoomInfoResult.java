package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiRoomInfoResult {
	@ZapcomApi(value = "房间名",remark = "房间名")
	private String name;
	@ZapcomApi(value = "房间id",remark = "房间id")
    private String roomid;
	@ZapcomApi(value = "封面图片",remark = "封面图片")
    private String cover_img;
	@ZapcomApi(value = "直播状态",remark = "101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期")
    private String live_status;
	@ZapcomApi(value = "开始时间",remark = "格式:yyyy-MM-dd HH:mm:s")
    private String start_time;
	@ZapcomApi(value = "结束时间",remark = "格式:yyyy-MM-dd HH:mm:ss")
    private String end_time;
	@ZapcomApi(value = "主播姓名",remark = "主播姓名")
    private String anchor_name;
	@ZapcomApi(value = "主播图片",remark = "主播图片")
    private String anchor_img;
	@ZapcomApi(value = "商品信息",remark = "商品信息")
    private List<ApiGoodsBeanResult> goods = new ArrayList<ApiGoodsBeanResult>();
	@ZapcomApi(value = "回放信息",remark = "回放信息")
    private List<ApiReplayResult> replays = new ArrayList<ApiReplayResult>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getCover_img() {
		return cover_img;
	}
	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}
	public String getLive_status() {
		return live_status;
	}
	public void setLive_status(String live_status) {
		this.live_status = live_status;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getAnchor_name() {
		return anchor_name;
	}
	public void setAnchor_name(String anchor_name) {
		this.anchor_name = anchor_name;
	}
	public String getAnchor_img() {
		return anchor_img;
	}
	public void setAnchor_img(String anchor_img) {
		this.anchor_img = anchor_img;
	}
	public List<ApiGoodsBeanResult> getGoods() {
		return goods;
	}
	public void setGoods(List<ApiGoodsBeanResult> goods) {
		this.goods = goods;
	}
	public List<ApiReplayResult> getReplays() {
		return replays;
	}
	public void setReplays(List<ApiReplayResult> replays) {
		this.replays = replays;
	}
	
    

    
}
