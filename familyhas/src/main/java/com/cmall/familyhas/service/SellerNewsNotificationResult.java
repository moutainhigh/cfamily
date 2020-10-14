package com.cmall.familyhas.service;


import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.NewsNotificationModel;
import com.srnpr.zapcom.baseclass.BaseClass;
/**
 * 转换输入类
 * @author shiyz
 *
 */
public class SellerNewsNotificationResult extends BaseClass {
	
	
	List<NewsNotificationModel> list = new  ArrayList<>();
	
	private String totalNews =  "";
	private String noReadNews  = "";
	private String pageSize  =  "";
	private String factoryCode = "";
	
	
	

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getTotalNews() {
		return totalNews;
	}

	public void setTotalNews(String totalNews) {
		this.totalNews = totalNews;
	}

	public String getNoReadNews() {
		return noReadNews;
	}

	public void setNoReadNews(String noReadNews) {
		this.noReadNews = noReadNews;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public List<NewsNotificationModel> getList() {
		return list;
	}

	public void setList(List<NewsNotificationModel> list) {
		this.list = list;
	}
	
	
	
}




