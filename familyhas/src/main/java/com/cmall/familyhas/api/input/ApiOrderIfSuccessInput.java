package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiOrderIfSuccessInput extends RootInput{
	@ZapcomApi(value="订单编号",require=1,remark="OS......")
	private String orderCode = "";
	@ZapcomApi(value="app系统名称",remark="IOS,Android")
	private String deviceType = "";
	@ZapcomApi(value="广告入口类型",demo="ADTP001",remark="ADTP001:个人中心 ,ADTP002:支付成功")
	private String adverEntrType = "";
	@ZapcomApi(value="渠道编号",demo="449748610001",remark="449748610001:APP/微信 ,449748610002:小程序,449748610003:LD短信支付,449748610004:无限制")
	private String channel_id = "449748610004";
	
	@ZapcomApi(value="LD短信支付特殊渠道编号拓展",demo="449748610003",remark="特定，只有LD短信支付成功页需要填写此值。如果是，入参填写:449748610003,目前为固定值，后期拓展再说")
	private String ldMsgPayChannel = "";
	
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getAdverEntrType() {
		return adverEntrType;
	}
	public void setAdverEntrType(String adverEntrType) {
		this.adverEntrType = adverEntrType;
	}
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getLdMsgPayChannel() {
		return ldMsgPayChannel;
	}
	public void setLdMsgPayChannel(String ldMsgPayChannel) {
		this.ldMsgPayChannel = ldMsgPayChannel;
	}
	
}
