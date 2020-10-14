package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiPaymentTypeAllInput extends RootInput{
	
	@ZapcomApi(value="订单编号",require=1)
	private String order_code = "";
	@ZapcomApi(value="app系统名称",remark="IOS,Android")
	private String deviceType = "";

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
