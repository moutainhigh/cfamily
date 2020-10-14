package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 取消订单  参数
 * @author wm
 *
 */
public class ApiOrderCancelInput extends RootInput {

	@ZapcomApi(value="订单号",remark="用于查询指定单时用",require = 1)
	private String orderCode = "";
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	@Override
	public String toString() {
		return "ApiOrderCancelInput [orderCode=" + orderCode + ", getOrderCode()=" + getOrderCode() + ", getVersion()="
				+ getVersion() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
