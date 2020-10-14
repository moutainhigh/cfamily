package com.cmall.familyhas.api.result;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCancelOrderResult extends RootResult {

	@ZapcomApi(value = "订单编号", require = 1, demo = "DD123456789")
	private String orderCode;
	@ZapcomApi(value = "取消原因", require = 1, demo = "")
	private String cancelReason;
	@ZapcomApi(value = "取消进度", require = 1, demo = "待审核，取消成功")
	private String cancelStatus;
	@ZapcomApi(value = "支付方式", require = 1, demo = "在线支付\n" + 
			"货到付款\n")
	private String payType;
	@ZapcomApi(value = "退款金额", require = 1, demo = "99.9")
	private BigDecimal returnMoney;
	@ZapcomApi(value = "取消说明", require = 1, demo = "99.9")
	private String cancelNotice;
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getCancelStatus() {
		return cancelStatus;
	}
	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public BigDecimal getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(BigDecimal returnMoney) {
		this.returnMoney = returnMoney;
	}
	public String getCancelNotice() {
		return cancelNotice;
	}
	public void setCancelNotice(String cancelNotice) {
		this.cancelNotice = cancelNotice;
	}
	
	
}
