package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetLiveRoomInfoResult extends RootResult {

	@ZapcomApi(value = "直播房间编号", remark = "")
	private String liveRoomId = "";
	
	@ZapcomApi(value = "主播用户编码", remark = "")
	private String liveMemberCode = "";
	
	@ZapcomApi(value = "房间标题", remark = "")
	private String liveTitle = "";
	
	@ZapcomApi(value = "房间状态", remark = "未开始(预告):449746320001 / 已结束(回放):449746320002 / 直播中:449746320003")
	private String liveStatus = "";
	
	@ZapcomApi(value = "直播拉流地址", remark = "用户观看直播")
	private String livePullUrl = "";
	
	@ZapcomApi(value = "直播推流地址", remark = "主播推送直播")
	private String livePushUrl = "";
	
	@ZapcomApi(value = "直播回放文件地址", remark = "观看回放")
	private String liveFileUrl = "";
	
	@ZapcomApi(value = "预计开播时间", remark = "")
	private String startTime = "";
	
	@ZapcomApi(value = "用户登录即时通信 IM 的密码", remark = "")
	private String userSig = "";
	
	@ZapcomApi(value = "群组号", remark = "")
	private String groupId = "";
	
	@ZapcomApi(value = "直播封面图", remark = "")
	private String liveCoverPicture = "";
	
	@ZapcomApi(value = "直播背景图", remark = "")
	private String liveBackgroundPicture = "";
	
	@ZapcomApi(value = "直播间支持功能", remark = "449746310001:评论;  449746310002:点赞;  449746310003:商品货架(多个功能用逗号隔开)")
	private String liveFunctions = "";
	
	@ZapcomApi(value = "主播头像", remark = "")
	private String avatar = "";
	
	@ZapcomApi(value = "主播昵称", remark = "")
	private String nickName = "";
	
	@ZapcomApi(value = "点赞数", remark = "")
	private int approveNum = 0;
	
	@ZapcomApi(value = "评论数", remark = "")
	private int evaluationNum = 0;
	
	@ZapcomApi(value = "主播还是用户", remark = "0:主播 ; 1:用户")
	private String anchorOrUser = "";
	
	@ZapcomApi(value = "用户头像", remark = "")
	private String userAvatar = "";
	
	@ZapcomApi(value = "用户昵称", remark = "")
	private String userNickName = "";

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getLiveMemberCode() {
		return liveMemberCode;
	}

	public void setLiveMemberCode(String liveMemberCode) {
		this.liveMemberCode = liveMemberCode;
	}

	public String getAnchorOrUser() {
		return anchorOrUser;
	}

	public void setAnchorOrUser(String anchorOrUser) {
		this.anchorOrUser = anchorOrUser;
	}

	public String getLivePullUrl() {
		return livePullUrl;
	}

	public void setLivePullUrl(String livePullUrl) {
		this.livePullUrl = livePullUrl;
	}

	public String getLivePushUrl() {
		return livePushUrl;
	}

	public void setLivePushUrl(String livePushUrl) {
		this.livePushUrl = livePushUrl;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getLiveFileUrl() {
		return liveFileUrl;
	}

	public void setLiveFileUrl(String liveFileUrl) {
		this.liveFileUrl = liveFileUrl;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getUserSig() {
		return userSig;
	}

	public void setUserSig(String userSig) {
		this.userSig = userSig;
	}

	public String getLiveCoverPicture() {
		return liveCoverPicture;
	}

	public void setLiveCoverPicture(String liveCoverPicture) {
		this.liveCoverPicture = liveCoverPicture;
	}

	public String getLiveBackgroundPicture() {
		return liveBackgroundPicture;
	}

	public void setLiveBackgroundPicture(String liveBackgroundPicture) {
		this.liveBackgroundPicture = liveBackgroundPicture;
	}

	public String getLiveFunctions() {
		return liveFunctions;
	}

	public void setLiveFunctions(String liveFunctions) {
		this.liveFunctions = liveFunctions;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getApproveNum() {
		return approveNum;
	}

	public void setApproveNum(int approveNum) {
		this.approveNum = approveNum;
	}

	public int getEvaluationNum() {
		return evaluationNum;
	}

	public void setEvaluationNum(int evaluationNum) {
		this.evaluationNum = evaluationNum;
	}
	
}
