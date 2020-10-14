package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAftermarketListInput extends RootInput {

	@ZapcomApi(value="下一页",require=1,demo="1")
	private int page = 0;
	
	@ZapcomApi(value="订单号",remark="非必填项，如果用户从订单详情进入，则需要传入订单号", require=0,demo="1")
	private String order_code = "";
	
	@ZapcomApi(value="售后单状态", require=0,demo="0：处理中，1：已完成")
	private String asale_status = "";

	
	public String getAsale_status() {
		return asale_status;
	}

	public void setAsale_status(String asale_status) {
		this.asale_status = asale_status;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
}
