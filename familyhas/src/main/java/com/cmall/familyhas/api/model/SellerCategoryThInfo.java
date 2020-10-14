package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class SellerCategoryThInfo {
	
	@ZapcomApi(value="分类编号")
	private String categoryCode="";
	
	@ZapcomApi(value="分类名称")
	private String categoryName="";

	@ZapcomApi(value="分类图片")
	private String categoryPic="";
	
	@ZapcomApi(value="广告图片")
	private String advPic="";
	
	@ZapcomApi(value="广告链接")
	private String advUrl="";
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryPic() {
		return categoryPic;
	}

	public void setCategoryPic(String categoryPic) {
		this.categoryPic = categoryPic;
	}

	public String getAdvPic() {
		return advPic;
	}

	public void setAdvPic(String advPic) {
		this.advPic = advPic;
	}

	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}

}
