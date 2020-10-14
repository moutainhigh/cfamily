package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductHotSale;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 今日热卖输出类
 */
public class ApiForTodayHotSaleResult extends RootResultWeb {
	
	@ZapcomApi(value="商品集合")
	private List<ProductHotSale> productList = new ArrayList<ProductHotSale>();
	
	@ZapcomApi(value="总页数")
	private int pagination=0;
	
	@ZapcomApi(value="浏览时长",remark="倒计时")
	private int browserTime=0;

	
	public int getBrowserTime() {
		return browserTime;
	}

	public void setBrowserTime(int browserTime) {
		this.browserTime = browserTime;
	}

	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}

	public List<ProductHotSale> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductHotSale> productList) {
		this.productList = productList;
	}
	
}