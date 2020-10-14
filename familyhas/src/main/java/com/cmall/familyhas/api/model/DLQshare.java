package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 大陆桥分享页
 * @author zw
 *
 */
public class DLQshare {
	
	@ZapcomApi(value = "封面专题名称(页面标题)")
	private String ad_name = "";
	
	@ZapcomApi(value = "分享标题")
	private String share_title = "";
	
	
	@ZapcomApi(value = "分享内容")
	private String share_content = "";
	
	
	@ZapcomApi(value = "分享图片")
	private String share_pic = "";


	public String getAd_name() {
		return ad_name;
	}


	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
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


	public String getShare_pic() {
		return share_pic;
	}


	public void setShare_pic(String share_pic) {
		this.share_pic = share_pic;
	}
	
	
	
	

}
