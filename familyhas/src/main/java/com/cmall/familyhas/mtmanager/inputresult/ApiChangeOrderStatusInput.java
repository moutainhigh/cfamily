package com.cmall.familyhas.mtmanager.inputresult;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiChangeOrderStatusInput extends RootInput {

	
	@ZapcomApi(value = "订单编号", remark = "",require=1, demo = "")
	private List<String> orderCode = new ArrayList<String>();
	
	@ZapcomApi(value = "订单状态", remark = "4497153900010001:下单成功-未付款\n"
			+ "4497153900010002:下单成功-未发货\n"
			+ "4497153900010003:已发货\n"
			+ "4497153900010004:已收货\n"
			+ "4497153900010005:交易成功\n"
			+ "4497153900010006:交易失败\n"
			+ "4497153900010007:交易无效\n"
			,require=1, demo = "")
	private String orderStatus = "";

	public List<String> getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(List<String> orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
