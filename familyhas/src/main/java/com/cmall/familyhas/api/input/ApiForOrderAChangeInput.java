package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForOrderAChangeInput extends RootInput {

	@ZapcomApi(value = "订单编号",require=1)
	private String order_code = "" ;
	
	@ZapcomApi(value = "操作类型，D为退货挽留")
	private String do_type = "" ;

	public String getDo_type() {
		return do_type;
	}

	public void setDo_type(String do_type) {
		this.do_type = do_type;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	
	
}
