package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetAppletPaySuccessResult extends RootResult {

	@ZapcomApi(value="小程序支付成功配置商品")
	private List<ProductMaybeLove> productMaybeLove = new ArrayList<ProductMaybeLove>();
	
	@ZapcomApi(value="分页总页数,该字段可能为0,为0的话则不用考虑分页,如果非0,则要考虑分页")
	private int pagination = 0;
	
	@ZapcomApi(value="标题")
	private String title = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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
