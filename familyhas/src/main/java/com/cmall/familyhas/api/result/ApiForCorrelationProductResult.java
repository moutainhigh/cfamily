package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.CorrelationProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForCorrelationProductResult extends RootResultWeb {
	
	@ZapcomApi(value = "关联商品列表")
	private List<CorrelationProduct> products = new ArrayList<CorrelationProduct>();
	
	@ZapcomApi(value = "关联商品的分类编号", remark = "多个编号逗号隔开")
	private String cCategory = "";

	public List<CorrelationProduct> getProducts() {
		return products;
	}

	public void setProducts(List<CorrelationProduct> products) {
		this.products = products;
	}

	public String getcCategory() {
		return cCategory;
	}

	public void setcCategory(String cCategory) {
		this.cCategory = cCategory;
	}

}
