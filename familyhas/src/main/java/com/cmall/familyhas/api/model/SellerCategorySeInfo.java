package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class SellerCategorySeInfo {
	
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
	
	@ZapcomApi(value="品牌List")
	private List<BrandInfo> brands= new ArrayList<BrandInfo>();
	
	@ZapcomApi(value="下级类目List",remark = "旧版本用，用于第二级直接存放第四级分类")
	private List<SellerCategoryThInfo> scs= new ArrayList<SellerCategoryThInfo>();
	
	@ZapcomApi(value="下级类目List",remark = "新属性，用于存放第三级分类")
	private List<SellerCategoryThreeInfo> scsThree= new ArrayList<SellerCategoryThreeInfo>();
	
	@ZapcomApi(value="分类编号",remark = "用于527以后版本,若值为1,则直接在二级分类下展示所有四级分类")
	private String isBfdCatefory="0";
	
	
	
	public String getIsBfdCatefory() {
		return isBfdCatefory;
	}

	public void setIsBfdCatefory(String isBfdCatefory) {
		this.isBfdCatefory = isBfdCatefory;
	}

	public List<SellerCategoryThreeInfo> getScsThree() {
		return scsThree;
	}

	public void setScsThree(List<SellerCategoryThreeInfo> scsThree) {
		this.scsThree = scsThree;
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

	public List<BrandInfo> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandInfo> brands) {
		this.brands = brands;
	}

	public List<SellerCategoryThInfo> getScs() {
		return scs;
	}

	public void setScs(List<SellerCategoryThInfo> scs) {
		this.scs = scs;
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
