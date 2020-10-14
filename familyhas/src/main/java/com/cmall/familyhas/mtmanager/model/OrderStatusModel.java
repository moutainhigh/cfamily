package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderStatusModel {
	/**
	 * 订单编号
	 */
	@ZapcomApi(value = "订单编号", remark = "", demo = "DD140212100029")
	private String OrderCode;
	@ZapcomApi(value = "订单状态", remark = "", demo = "4497153900010002")
	private String OrderStstus;
	@ZapcomApi(value = "运单编号", remark = "", demo = "1700121934697")
	private String WayBIll;
	@ZapcomApi(value = "物流名称", remark = "", demo = "顺丰")
	private String LogisticseName;
	@ZapcomApi(value = "发货时间", remark = "", demo = "2015-11-11 11:11:11")
	private String sendTime;
	public String getOrderCode() {
		return OrderCode;
	}

	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}

	public String getOrderStstus() {
		return OrderStstus;
	}

	public void setOrderStstus(String orderStstus) {
		OrderStstus = orderStstus;
	}

	public String getWayBIll() {
		return WayBIll;
	}

	public void setWayBIll(String wayBIll) {
		WayBIll = wayBIll;
	}

	public String getLogisticseName() {
		return LogisticseName;
	}

	public void setLogisticseName(String logisticseName) {
		LogisticseName = logisticseName;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}
