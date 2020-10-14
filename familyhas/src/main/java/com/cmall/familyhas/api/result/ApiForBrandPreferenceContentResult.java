package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.BrandPic;
import com.cmall.familyhas.api.model.BrandProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 品牌特惠列表输出类
 * 
 * @author guz
 *
 */
public class ApiForBrandPreferenceContentResult extends RootResultWeb{
	
	@ZapcomApi(value = "折扣")
	private String discount = "";
	
	@ZapcomApi(value = "开始时间",demo="2015-03-05 12:09:08")
	private String upTime = "";
	
	@ZapcomApi(value = "结束时间",demo="2015-03-05 12:09:08")
	private String downTime = "";
	
	@ZapcomApi(value = "广告图List")
	private List<BrandPic> brandPicList = new ArrayList<BrandPic>();
	
	@ZapcomApi(value = "商品List")
	private List<BrandProduct> productList = new ArrayList<BrandProduct>();

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public List<BrandPic> getBrandPicList() {
		return brandPicList;
	}

	public void setBrandPicList(List<BrandPic> brandPicList) {
		this.brandPicList = brandPicList;
	}

	public List<BrandProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<BrandProduct> productList) {
		this.productList = productList;
	}
	
}
