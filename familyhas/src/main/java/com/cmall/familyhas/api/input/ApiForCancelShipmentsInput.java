package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCancelShipmentsInput extends RootInput {

	@ZapcomApi(value = "订单号", remark = "以DD开头的订单号", require = 1, demo = "DD32246104")
	private String orderCode = "";

	@ZapcomApi(value = "取消原因", remark = "取消原因,具体查看取消原因接口", require = 1, demo = "RGR160308100001")
	private String reason = "";

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
