package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.JulyPopularizeProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/** 
* @author Angel Joy
* @Time 2020年6月11日 上午10:39:01 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForJulyPopularizeResult extends RootResult {
	
	@ZapcomApi(value="商品列表",remark="",demo="")
	private List<JulyPopularizeProduct> goods=new ArrayList<JulyPopularizeProduct>();
	
	@ZapcomApi(value="当前页码",remark="1",demo="1")
	private Integer currentPage=1;
	
	@ZapcomApi(value="总页数",remark="10",demo="10")
	private Integer totalPage=10;

	public List<JulyPopularizeProduct> getGoods() {
		return goods;
	}

	public void setGoods(List<JulyPopularizeProduct> goods) {
		this.goods = goods;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
