package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 获取评论内容
 * @author fq
 *
 */
public class DLQCommentModel {
	
	@ZapcomApi(value="评论人手机号")
	private String mobile = "";
	
	@ZapcomApi(value="客户端ip")
	private String c_ip = "";
	
	@ZapcomApi(value="评论内容")
	private String content = "";
	
	@ZapcomApi(value="评论时间")
	private String c_time = "";
	
	@ZapcomApi(value="头像")
	private String head_photo = "";
	
	@ZapcomApi(value="追评时间")
	private String rtn_time = "";
	
	@ZapcomApi(value="追评内容")
	private String rtn_content = "";
	
	@ZapcomApi(value="追评用户")
	private String rtn_user = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getC_ip() {
		return c_ip;
	}

	public void setC_ip(String c_ip) {
		this.c_ip = c_ip;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getC_time() {
		return c_time;
	}

	public void setC_time(String c_time) {
		this.c_time = c_time;
	}

	public String getHead_photo() {
		return head_photo;
	}

	public void setHead_photo(String head_photo) {
		this.head_photo = head_photo;
	}

	public String getRtn_time() {
		return rtn_time;
	}

	public void setRtn_time(String rtn_time) {
		this.rtn_time = rtn_time;
	}

	public String getRtn_content() {
		return rtn_content;
	}

	public void setRtn_content(String rtn_content) {
		this.rtn_content = rtn_content;
	}

	public String getRtn_user() {
		return rtn_user;
	}

	public void setRtn_user(String rtn_user) {
		this.rtn_user = rtn_user;
	}
	
	
	
}
