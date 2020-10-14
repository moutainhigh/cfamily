package com.cmall.familyhas.api.result.apphome;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class AppHomeActivityResult extends RootResultWeb{
	
	@ZapcomApi(value="ID",remark="")
	public int zid;
	@ZapcomApi(value="UID",remark="活动唯一标识")
	public String uid;
	@ZapcomApi(value="标题",remark="活动标题")
	public String title;
	@ZapcomApi(value="开始时间",remark="活动在页面展示开始时间")
	public String start_time;
	@ZapcomApi(value="结束时间",remark="活动在页面展示结束时间")
	public String end_time;
	@ZapcomApi(value="图片",remark="活动宣传图")
	public String pic_url;
	@ZapcomApi(value="地点",remark="活动地点")
	public String activity_location;
	@ZapcomApi(value="活动举办开始时间",remark="")
	public String activity_start_time;
	@ZapcomApi(value="活动报名截止时间",remark="")
	public String join_up_end_time;
	@ZapcomApi(value="活动允许参加最大人数",remark="")
	public int activity_max_people;
	@ZapcomApi(value="channelID",remark="备用字段")
	public String channel_uid;
	@ZapcomApi(value="位置",remark="活动展示位置")
	public int seq;
	@ZapcomApi(value="活动报名所需信息",remark="姓名，电话，身份证号，所在城市分别对应449748140001,449748140002,449748140003,449748140004。")
	public String need_info;//姓名，电话，身份证号，所在城市，分别对应449748140001,449748140002,449748140003,449748140004。举例：名字，电话为必填项次数数据为”449748140001,449748140002“，用逗号隔开。只需要名字则数据为“1”。
	@ZapcomApi(value="活动描述",remark="活动介绍图文详情")
	public String activity_desc;//活动描述
	@ZapcomApi(value="分享描述",remark="分享描述")
	public String share_desc;//活动描述
	@ZapcomApi(value="活动是否过期",remark="过期：true，未过期：false")
	public boolean is_time_out = false;
	@ZapcomApi(value="活动报名提示",remark="立即报名,报名已截止,已结束.")
	public String is_time_out_remark = "立即报名";
	@ZapcomApi(value="是否参与活动了",remark="已报名：true，未报名：false")
	public boolean is_joined = false;
	
	
	public String getShare_desc() {
		return share_desc;
	}
	public void setShare_desc(String share_desc) {
		this.share_desc = share_desc;
	}
	public boolean isIs_joined() {
		return is_joined;
	}
	public void setIs_joined(boolean is_joined) {
		this.is_joined = is_joined;
	}
	public String getIs_time_out_remark() {
		return is_time_out_remark;
	}
	public void setIs_time_out_remark(String is_time_out_remark) {
		this.is_time_out_remark = is_time_out_remark;
	}
	public boolean isIs_time_out() {
		return is_time_out;
	}
	public void setIs_time_out(boolean is_time_out) {
		this.is_time_out = is_time_out;
	}
	public int getZid() {
		return zid;
	}
	public void setZid(int zid) {
		this.zid = zid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getActivity_location() {
		return activity_location;
	}
	public void setActivity_location(String activity_location) {
		this.activity_location = activity_location;
	}
	public String getActivity_start_time() {
		return activity_start_time;
	}
	public void setActivity_start_time(String activity_start_time) {
		this.activity_start_time = activity_start_time;
	}
	public String getJoin_up_end_time() {
		return join_up_end_time;
	}
	public void setJoin_up_end_time(String join_up_end_time) {
		this.join_up_end_time = join_up_end_time;
	}
	public int getActivity_max_people() {
		return activity_max_people;
	}
	public void setActivity_max_people(int activity_max_people) {
		this.activity_max_people = activity_max_people;
	}
	public String getChannel_uid() {
		return channel_uid;
	}
	public void setChannel_uid(String channel_uid) {
		this.channel_uid = channel_uid;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getNeed_info() {
		return need_info;
	}
	public void setNeed_info(String need_info) {
		this.need_info = need_info;
	}
	public String getActivity_desc() {
		return activity_desc;
	}
	public void setActivity_desc(String activity_desc) {
		this.activity_desc = activity_desc;
	}
	
}
