package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.PcItems;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 品牌特惠列表输出类
 * 
 * @author guz
 *
 */
public class ApiForPcBrandPreferenceResult extends RootResultWeb{
	
	@ZapcomApi(value = "系统时间",demo="yyyy-MM-dd HH:mm:ss")
	private String sysTime = "";
	
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
	private List<PcItems> items = new ArrayList<PcItems>();
	
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
	public List<PcItems> getItems() {
		return items;
	}
	public void setItems(List<PcItems> items) {
		this.items = items;
	}
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	
}
