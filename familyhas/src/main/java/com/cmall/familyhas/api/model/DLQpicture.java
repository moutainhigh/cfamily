package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 大陆桥轮播图
 * @author zw
 *
 */
public class DLQpicture {
	

	@ZapcomApi(value = "图片")
	private String pic = "";
	

	@ZapcomApi(value = "开始时间")
	private String start_time = "";
	
	
	@ZapcomApi(value = "结束时间")
	private String end_time = "";
	

	@ZapcomApi(value = "URL")
	private String url = "";
	

	@ZapcomApi(value = "位置")
	private String location = "";
	

	@ZapcomApi(value = "更新时间")
	private String update_time = "";
	
	@ZapcomApi(value = "广告标识")
	private String id_number = "";
	
	@ZapcomApi(value = "删除状态")
	private String delete_state = "";
	
	@ZapcomApi(value = "页面编号")
	private String page_number = "";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getDelete_state() {
		return delete_state;
	}

	public void setDelete_state(String delete_state) {
		this.delete_state = delete_state;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

	
	
}
