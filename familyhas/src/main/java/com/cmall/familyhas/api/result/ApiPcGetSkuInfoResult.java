package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Category;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiPcGetSkuInfoResult extends ApiGetSkuInfoResult {

	@ZapcomApi(value="商品分类列表",remark="按照从大到小排序")
	private List<Category> categoryList = new ArrayList<Category>();

	/**
	 * 获取  categoryList
	 */
	public List<Category> getCategoryList() {
		return categoryList;
	}

	/**
	 * 设置 
	 * @param categoryList 
	 */
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	
	
}
