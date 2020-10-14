package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ImagePropertyResult extends RootResultWeb{
	@ZapcomApi(value = "是否成功：error|success")
	private String status = "";
	@ZapcomApi(value = "返回结果消息提示")
	private String msg = "";
	@ZapcomApi(value = "图片宽度")
	private Integer width = 10;
	@ZapcomApi(value = "图片高度")
	private Integer height = 10;
	@ZapcomApi(value = "图片所占存储空间大小")
	private String size = "0";
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
