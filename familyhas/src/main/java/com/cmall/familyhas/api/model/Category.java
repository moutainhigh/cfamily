package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Category {
	@ZapcomApi(value="分类名称")
	private String categoryName="";
	@ZapcomApi(value="分类编号")
	private String categoryCode="";
	/**
	 * 获取  categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * 设置 
	 * @param categoryName 
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * 获取  categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}
	/**
	 * 设置 
	 * @param categoryCode 
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	
}
