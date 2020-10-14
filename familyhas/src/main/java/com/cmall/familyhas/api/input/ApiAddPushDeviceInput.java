package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 推送设备信息输入参数
 * @author fq
 *
 */
public class ApiAddPushDeviceInput extends RootInput{

	@ZapcomApi(value="百度推送的channelId",require=1)
	private String channelId = "";
	
	@ZapcomApi(value="token",remark="token为空则为解绑")
	private String token = "";
	
	@ZapcomApi(value="用户标识")
	private String userPhone = "";
	
	@ZapcomApi(value="设备类型",remark="2:IOS;1:ANDROID",require=1)
	private String deviceType = "";
	
	@ZapcomApi(value="信鸽推送的deviceToken")
	private String deviceToken = "";
	
	@ZapcomApi(value="设备通知开关状态",remark="0为开启，1为关闭")
	private String noticeSwitch = "0";
	
	@ZapcomApi(value="app版本",remark="app版本")
	private String app_vision = "";
	
	
	
	public String getApp_vision() {
		return app_vision;
	}

	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getNoticeSwitch() {
		return noticeSwitch;
	}

	public void setNoticeSwitch(String noticeSwitch) {
		this.noticeSwitch = noticeSwitch;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	
	
	
}
