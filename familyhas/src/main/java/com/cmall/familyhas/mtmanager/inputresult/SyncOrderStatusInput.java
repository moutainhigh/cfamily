package com.cmall.familyhas.mtmanager.inputresult;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class SyncOrderStatusInput extends RootInput {
	@ZapcomApi(value = "订单编号", require = 1, remark = "", demo = "DD140212100029,DD140212100030")
	private String orderCodes = "";

	public String getOrderCodes() {
		return orderCodes;
	}

	public void setOrderCodes(String orderCodes) {
		this.orderCodes = orderCodes;
	}

}
