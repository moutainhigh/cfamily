package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiAfterSaleListResult extends RootResultWeb{
	@ZapcomApi(value="总页数")
	private int countPage = 0;
	@ZapcomApi(value="当前页数")
	private int nowPage = 0;
	@ZapcomApi(value="商品订单信息")
	private List<ApiSellerOrderListResult> sellerOrderList = new ArrayList<ApiSellerOrderListResult>();
	
	public List<ApiSellerOrderListResult> getSellerOrderList() {
		return sellerOrderList;
	}
	public void setSellerOrderList(List<ApiSellerOrderListResult> sellerOrderList) {
		this.sellerOrderList = sellerOrderList;
	}
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
	
}
