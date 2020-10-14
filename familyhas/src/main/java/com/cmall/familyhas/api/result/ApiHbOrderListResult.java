package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Reason;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiHbOrderListResult extends RootResultWeb{
	
	@ZapcomApi(value="惠币商品订单信息")
	private List<ApiForQueryHbAgentOrderInfo> sellerOrderList = new ArrayList<ApiForQueryHbAgentOrderInfo>();
	
	@ZapcomApi(value="总页数")
	private int countPage = 0;
	
	@ZapcomApi(value="当前页数")
	private int nowPage = 0;
	

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

	public List<ApiForQueryHbAgentOrderInfo> getSellerOrderList() {
		return sellerOrderList;
	}

	public void setSellerOrderList(List<ApiForQueryHbAgentOrderInfo> sellerOrderList) {
		this.sellerOrderList = sellerOrderList;
	}
	
	
	
}
