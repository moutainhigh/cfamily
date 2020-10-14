package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 下单后再次购买接口输入参数
 * @author fq
 *
 */
public class ApiProductToBuyInput extends RootInput{

	@ZapcomApi(value="订单编号",require = 1)
	private String order_code = "";

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	
	
	
}
