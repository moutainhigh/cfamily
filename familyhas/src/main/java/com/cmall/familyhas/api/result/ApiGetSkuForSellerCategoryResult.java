package com.cmall.familyhas.api.result;


import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.model.SaleProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 商品列表输出类
 * @author 李国杰
 * date 2014-09-21
 * @version 1.0
 */
public class ApiGetSkuForSellerCategoryResult extends RootResultWeb{
	
	@ZapcomApi(value = "商品LIST")
	private List<SaleProduct> products = new ArrayList<SaleProduct>();
	
	@ZapcomApi(value = "该类别下商品总数")
	private String count = "";

	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();

	public List<SaleProduct> getProducts() {
		return products;
	}

	public void setProducts(List<SaleProduct> products) {
		this.products = products;
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
