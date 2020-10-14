package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 查询分销粉丝输出类
 */
public class ApiForAgentQueryOrderResult extends RootResult {
	
	@ZapcomApi(value="总页数")
	private int countPage = 0;
	
	@ZapcomApi(value="当前页数")
	private int nowPage = 0;
	
	@ZapcomApi(value="分销订单信息")
	private List<ApiForQueryAgentOrderInfo> orderList = new ArrayList<ApiForQueryAgentOrderInfo>();

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public List<ApiForQueryAgentOrderInfo> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<ApiForQueryAgentOrderInfo> orderList) {
		this.orderList = orderList;
	}
}
