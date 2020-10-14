package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiDgGetRecommendResult extends RootResult {
	@ZapcomApi(value="猜你喜欢推荐商品")
	private List<ProductMaybeLove> productMaybeLove = new ArrayList<ProductMaybeLove>();
	@ZapcomApi(value="分页总页数,该字段可能为0,为0的话则不用考虑分页,如果非0,则要考虑分页")
	private int pagination = 0;
	
	public List<ProductMaybeLove> getProductMaybeLove() {
		return productMaybeLove;
	}
	public void setProductMaybeLove(List<ProductMaybeLove> productMaybeLove) {
		this.productMaybeLove = productMaybeLove;
	}
	
	public int getPagination() {
		return pagination;
	}
	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
}
