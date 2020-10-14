package com.cmall.familyhas.api.result;

import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐商品结果
 * @author pang_jhui
 *
 */
public class ApiRecProductInfoResult extends RootResultWeb{
	
	@ZapcomApi(value="百分点推荐商品")
	private List<ProductMaybeLove> productMaybeLove = new ArrayList<ProductMaybeLove>();
	
	@ZapcomApi(value="推荐栏名称")
	private String recBarName = "";
	
	@ZapcomApi(value="分页总页数")
	private int pagination=0;
	
	
	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}

	public List<ProductMaybeLove> getProductMaybeLove() {
		return productMaybeLove;
	}

	public void setProductMaybeLove(List<ProductMaybeLove> productMaybeLove) {
		this.productMaybeLove = productMaybeLove;
	}

	public String getRecBarName() {
		return recBarName;
	}

	public void setRecBarName(String recBarName) {
		this.recBarName = recBarName;
	}
    
}
