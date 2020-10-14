package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetLiveRoomProductsResult extends RootResultWeb{

	private int totalPage = 0;
	private int totalNum = 0;
	private int currentPage=0;
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	private List<MDataMap> productList = new ArrayList<>();
	
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<MDataMap> getProductList() {
		return productList;
	}
	public void setProductList(List<MDataMap> productList) {
		this.productList = productList;
	}
	
	
	
}
