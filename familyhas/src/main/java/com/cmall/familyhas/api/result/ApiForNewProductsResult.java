package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Items;
import com.cmall.familyhas.api.model.NewProducts;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 新品推荐输出类
 * 
 * @author guz
 *
 */
public class ApiForNewProductsResult extends RootResultWeb{
	
	@ZapcomApi(value = "栏目图片",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String banner_img = "";
	
	@ZapcomApi(value = "栏目名称",demo="品牌")
	private String banner_name = "";
	
	@ZapcomApi(value = "链接地址",demo="www.baidu.com")
	private String banner_link = "";
	
	public String getBanner_link() {
		return banner_link;
	}
	public void setBanner_link(String banner_link) {
		this.banner_link = banner_link;
	}
	@ZapcomApi(value = "栏目详情")
	private List<NewProducts> items = new ArrayList<NewProducts>();
	
	public String getBanner_img() {
		return banner_img;
	}
	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}
	public String getBanner_name() {
		return banner_name;
	}
	public void setBanner_name(String banner_name) {
		this.banner_name = banner_name;
	}
	public List<NewProducts> getItems() {
		return items;
	}
	public void setItems(List<NewProducts> items) {
		this.items = items;
	}

}
