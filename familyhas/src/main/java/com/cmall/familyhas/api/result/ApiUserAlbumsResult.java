package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiUserAlbumsResult {
	@ZapcomApi(value = "相册编号")
	private String album_id = "";
	@ZapcomApi(value = "相册预览图")
	private String album_preview_img = "";
	@ZapcomApi(value = "模板标题")
	private String template_title = "";
	@ZapcomApi(value = "相册创建时间")
	private String album_date = "";
	@ZapcomApi(value = "分享标题")
	private String share_title = "";
	@ZapcomApi(value = "分享图片")
	private String share_img = "";
	
	
	
	
	public String getAlbum_date() {
		return album_date;
	}
	public void setAlbum_date(String album_date) {
		this.album_date = album_date;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getAlbum_preview_img() {
		return album_preview_img;
	}
	public void setAlbum_preview_img(String album_preview_img) {
		this.album_preview_img = album_preview_img;
	}
	public String getTemplate_title() {
		return template_title;
	}
	public void setTemplate_title(String template_title) {
		this.template_title = template_title;
	}
	public String getShare_title() {
		return share_title;
	}
	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}
	public String getShare_img() {
		return share_img;
	}
	public void setShare_img(String share_img) {
		this.share_img = share_img;
	}
	
}
