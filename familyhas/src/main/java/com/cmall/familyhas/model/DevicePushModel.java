package com.cmall.familyhas.model;


import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class DevicePushModel {

	@ZapcomApi(value="用户手机号")
	private String mobile = "";
	
	@ZapcomApi(value="设备编号")
	private String channel_id = "";
	
	@ZapcomApi(value="设备类型",remark="2:IOS;1:ANDROID")
	private String device_type = "";
	
	@ZapcomApi(value="更新时间")
	private String update_time = "";
	
	@ZapcomApi(value="绑定状态")
	private String bund_status = "";
	
	@ZapcomApi(value="信鸽推送的deviceToken")
	private String device_token = "";
	
	@ZapcomApi(value="设备通知开关状态",remark="0为开启，1为关闭")
	private String noticeSwitch = "0";
	
	@ZapcomApi(value="设备通知是否支持信鸽",remark="0为支持，1为不支持")
	private String supportXinGe = "1";

	@ZapcomApi(value="app版本",remark="app版本")
	private String app_vision = "";
	
	public String getApp_vision() {
		return app_vision;
	}

	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}
	

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getSupportXinGe() {
		return supportXinGe;
	}

	public void setSupportXinGe(String supportXinGe) {
		this.supportXinGe = supportXinGe;
	}

	public String getNoticeSwitch() {
		return noticeSwitch;
	}

	public void setNoticeSwitch(String noticeSwitch) {
		this.noticeSwitch = noticeSwitch;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getBund_status() {
		return bund_status;
	}

	public void setBund_status(String bund_status) {
		this.bund_status = bund_status;
	}
	
	
	 
}
