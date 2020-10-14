package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSmallProceduresSubscribeInput extends RootInput {
	
	@ZapcomApi(value="活动编号",remark="订阅通知的活动编号",require=1)
	private String eventCode = "";
	
	@ZapcomApi(value="商品编号",remark="订阅通知的商品编号",require=1)
	private String productCode = "";
	
	@ZapcomApi(value="小程序标识",remark="小程序标识，调用推送消息网关用")
	private String formId = "123456";
	
	@ZapcomApi(value="用户标识",remark="用户标识，调用推送消息网关用",require=1)
	private String openId = "";
	
	@ZapcomApi(value="请求来源",remark="来源：(xcx:小程序)，(wxshop:微信商城)，(ios:苹果APP) ,(android:安卓APP)", require=0,demo="xcx")
	private String source = "";
	
	@ZapcomApi(value="预约类型",remark="1：预约，0：取消预约", require=1)
	private String type = "";
	
	@ZapcomApi(value="设置推送时间",remark="活动开始前X分钟前通知。X为入参数字，非必填项，如果为空，则默认距活动开始5分钟前推送",require=0)
	private String time = "";

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	

}
