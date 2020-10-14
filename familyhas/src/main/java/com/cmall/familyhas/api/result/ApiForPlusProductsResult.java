package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/** 
* @author Angel Joy
* @Time 2020年5月11日 下午1:09:53 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusProductsResult extends RootResult {
	
	@ZapcomApi(value="当前页",remark="当前页码",demo="1")
	private Integer curentPage;
	
	@ZapcomApi(value="总页码",remark="总页码,目前最多10页",demo="10")
	private Integer totalPage;
	
	@ZapcomApi(value="商品列表",remark="")
	private List<PlusSaleProduct> products = new ArrayList<PlusSaleProduct>();

	public List<PlusSaleProduct> getProducts() {
		return products;
	}

	public void setProducts(List<PlusSaleProduct> products) {
		this.products = products;
	}

	public Integer getCurentPage() {
		return curentPage;
	}

	public void setCurentPage(Integer curentPage) {
		this.curentPage = curentPage;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
}
