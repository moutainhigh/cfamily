package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAgentQueryOrderInput extends RootInput {
	
	@ZapcomApi(value = "下一页", require = 1)
	private String nextPage = "1";
	@ZapcomApi(value = "订单状态", remark = "为空则默认查询全部,下单成功-未付款:4497153900010001,下单成功-未发货:4497153900010002,已发货:4497153900010003,已收货:4497153900010004,交易成功:4497153900010005,交易失败:4497153900010006")
	private String orderStatus = "";
	@ZapcomApi(value = "查询分销类型", require = 1, remark = "我分销的订单： 1， 粉丝分销的订单： 2")
	private String queryType = "1";
	@ZapcomApi(value = "查询开始日期", remark = "2020-04-21")
	private String startDate = "";
	@ZapcomApi(value = "查询结束日期", remark = "2020-04-21")
	private String endDate = "";
	
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
