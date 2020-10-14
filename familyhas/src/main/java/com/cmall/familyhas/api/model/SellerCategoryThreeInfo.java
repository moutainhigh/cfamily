package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class SellerCategoryThreeInfo {
	
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
	
	@ZapcomApi(value="下级类目List")
	private List<SellerCategoryThInfo> scs= new ArrayList<SellerCategoryThInfo>();
	
	
	
	public List<SellerCategoryThInfo> getScs() {
		return scs;
	}

	public void setScs(List<SellerCategoryThInfo> scs) {
		this.scs = scs;
	}

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
