package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 跨境通订单轨迹输入参数
 * @author pangjh
 *
 */
public class ApiKJTOrderTraceInput extends RootInput {
	
	
	@ZapcomApi(value="订单编号",require=1)
	private String order_code;

	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOrder_code() {
		return order_code;
	}

	/**
	 * 设置订单编号
	 * @param order_code
	 */
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

}
