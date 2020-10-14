package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiDLQCommentAddInput extends RootInput{
	
	@ZapcomApi(value="用户手机号")
	private String mobile = "";
	
	@ZapcomApi(value="ip")
	private String ip = "";
	
	@ZapcomApi(value="评论内容")
	private String content = "";
	
	@ZapcomApi(value="来源编号",require=1)
	private String rel_source = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRel_source() {
		return rel_source;
	}

	public void setRel_source(String rel_ssource) {
		this.rel_source = rel_ssource;
	}
	
	
}
