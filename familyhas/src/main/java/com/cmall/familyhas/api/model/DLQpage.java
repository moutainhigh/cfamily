package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 大陆桥页面
 * @author zw
 *
 */
public class DLQpage {
	
	@ZapcomApi(value = "专题名称")
	private String special_name = "";
	
	@ZapcomApi(value = "菜系名称")
	private String cuisine_name = "";
	
	@ZapcomApi(value = "菜系图片")
	private String cuisine_picture = "";
	
	@ZapcomApi(value = "状态")
	private String state = "";
	
	@ZapcomApi(value = "创建人")
	private String creater = "";
	
	@ZapcomApi(value = "创建时间")
	private String create_time = "";
	
	@ZapcomApi(value = "视频地址")
	private String url = "";
	
	@ZapcomApi(value = "页面编号")
	private String page_number = "";

	public String getSpecial_name() {
		return special_name;
	}

	public void setSpecial_name(String special_name) {
		this.special_name = special_name;
	}

	public String getCuisine_name() {
		return cuisine_name;
	}

	public void setCuisine_name(String cuisine_name) {
		this.cuisine_name = cuisine_name;
	}

	public String getCuisine_picture() {
		return cuisine_picture;
	}

	public void setCuisine_picture(String cuisine_picture) {
		this.cuisine_picture = cuisine_picture;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}


}
