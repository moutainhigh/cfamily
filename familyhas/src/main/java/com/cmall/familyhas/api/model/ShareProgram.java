package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ShareProgram {

	@ZapcomApi(value = "分享图片")
	private String share_img_url = "";
	
	@ZapcomApi(value = "分享标题")
	private String share_title = "";
	
	@ZapcomApi(value = "分享内容")
	private String share_content = "";
	
	@ZapcomApi(value="分享连接")
	private String share_link = "";

	public String getShare_img_url() {
		return share_img_url;
	}

	public void setShare_img_url(String share_img_url) {
		this.share_img_url = share_img_url;
	}

	public String getShare_title() {
		return share_title;
	}

	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getShare_link() {
		return share_link;
	}

	public void setShare_link(String share_link) {
		this.share_link = share_link;
	}
	
	
}
