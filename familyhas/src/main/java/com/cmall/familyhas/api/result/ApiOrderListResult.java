package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Reason;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiOrderListResult extends RootResultWeb{
	@ZapcomApi(value="总页数")
	private int countPage;
	@ZapcomApi(value="当前页数")
	private int nowPage;
	
	@ZapcomApi(value="商品订单信息")
	private List<ApiSellerOrderListResult> sellerOrderList = new ArrayList<ApiSellerOrderListResult>();
	
	@ZapcomApi(value = "取消发货原因列表", require = 1)
	private List<Reason> reasonList=new ArrayList<Reason>();
	
	
	public List<Reason> getReasonList() {
		return reasonList;
	}
	public void setReasonList(List<Reason> reasonList) {
		this.reasonList = reasonList;
	}
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
