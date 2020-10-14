package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 猜你喜欢分页对象
 * @author pang_jhui
 *
 */
public class ProductMaybeLovePage {
	
	/*猜你喜欢商品信息列表*/
	private List<ProductMaybeLove> productMaybeLove = new ArrayList<ProductMaybeLove>();
	
	/*总页数*/
	private int pageCount = 0;

	/**
	 * 猜你喜欢商品列表
	 * @return
	 */
	public List<ProductMaybeLove> getProductMaybeLove() {
		return productMaybeLove;
	}

	/**
	 * 设置猜你喜欢的商品列表
	 * @param productMaybeLove
	 */
	public void setProductMaybeLove(List<ProductMaybeLove> productMaybeLove) {
		this.productMaybeLove = productMaybeLove;
	}

	/**
	 * 总页数
	 * @return
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 设置总页数
	 * @param pageCount
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
