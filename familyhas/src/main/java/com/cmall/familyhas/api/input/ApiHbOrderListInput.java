package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHbOrderListInput extends RootInput{
	@ZapcomApi(value = "订单状态", remark = "为空则默认查询全部,下单成功-未付款:4497153900010001,下单成功-未发货:4497153900010002,已发货:4497153900010003,已收货:4497153900010004,交易成功:4497153900010005,交易失败:4497153900010006")
	private String orderStatus = "";
	@ZapcomApi(value="下一页",require=1)
	private String nextPage="1";
	
	
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	
	
}
