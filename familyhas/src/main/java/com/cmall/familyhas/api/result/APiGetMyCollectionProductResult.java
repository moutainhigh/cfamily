package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.CollectionProductModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiGetMyCollectionProductResult extends RootResultWeb {
	@ZapcomApi(value = "收藏商品列表")
	private  List<CollectionProductModel> collectionProductList = new ArrayList<CollectionProductModel>();
	
	@ZapcomApi(value="分页总页数",remark="分页总页数")
	private int pagination=0;
	
	public List<CollectionProductModel> getCollectionProductList() {
		return collectionProductList;
	}

	public void setCollectionProductList(
			List<CollectionProductModel> collectionProductList) {
		this.collectionProductList = collectionProductList;
	}

	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
	
}
