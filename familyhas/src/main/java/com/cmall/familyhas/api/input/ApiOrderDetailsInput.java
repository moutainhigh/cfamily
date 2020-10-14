package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 订单详情输入参数
 * @author wz
 *
 */
public class ApiOrderDetailsInput extends RootInput{
	@ZapcomApi(value="买家编号")
	private String buyer_code="";
	@ZapcomApi(value="订单编号",require=1)
	private String order_code="";
	@ZapcomApi(value="app系统名称",remark="IOS,Android")
	private String deviceType = "";
	@ZapcomApi(value="app版本",remark="IOS,Android,Wechat")
	private String app_vision = "";
	
	
	
	public String getApp_vision() {
		return app_vision;
	}
	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}
	public String getBuyer_code() {
		return buyer_code;
	}
	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
