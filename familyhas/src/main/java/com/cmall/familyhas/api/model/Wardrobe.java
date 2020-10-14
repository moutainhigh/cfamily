package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Wardrobe {

	@ZapcomApi(value="衣橱描述")
	private String describe = "";
	
	@ZapcomApi(value="分享图片")
	private String share_img = "";
	
	@ZapcomApi(value="分享标题")
	private String share_title = "";
	
	@ZapcomApi(value="分享内容")
	private String share_content = "";
	
	@ZapcomApi(value="分享连接")
	private String share_link = "";
	
	@ZapcomApi(value="衣橱编号")
	private String wardrobe_code = "";

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getShare_img() {
		return share_img;
	}

	public void setShare_img(String share_img) {
		this.share_img = share_img;
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

	public String getWardrobe_code() {
		return wardrobe_code;
	}

	public void setWardrobe_code(String wardrobe_code) {
		this.wardrobe_code = wardrobe_code;
	}
	
	
}
