package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiCancelOrderForIqiyiInput extends RootInput {

	
	@ZapcomApi(value = "验签串", remark = "md5(订单号+渠道用户号)",require=1, demo = "5615188c4c4c4bebaa2e50eaeb1caad5")
	private String signature = "";
	
	@ZapcomApi(value = "订单编号", remark = "订单编号",require=1, demo = "12465798")
	private String order_code = "";
	
	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
