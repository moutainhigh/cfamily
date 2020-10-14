package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class PushMsgInput extends RootInput {

	@ZapcomApi(value = "手机号码", require = 1, remark = "手机号码,多个用户逗号隔开")
	private String mobile = "";
	@ZapcomApi(value = "推送标题", require = 1, remark = "")
	private String title = "";
	@ZapcomApi(value = "推送内容", require = 1, remark = "")
	private String content = "";	
	@ZapcomApi(value = "推送类型", require = 1, remark = "01:链接,02：专题,03:商品")
	private String push_type = "" ;
	@ZapcomApi(value = "推送链接地址", require = 1, remark = "如果跳商品详情 格式：商品编号+|+商品名称")
	private String push_url = "" ;
	@ZapcomApi(value = "操作编号")
	private String operate_id;
	
	public String getOperate_id() {
		return operate_id;
	}
	public void setOperate_id(String operate_id) {
		this.operate_id = operate_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPush_type() {
		return push_type;
	}
	public void setPush_type(String push_type) {
		this.push_type = push_type;
	}
	public String getPush_url() {
		return push_url;
	}
	public void setPush_url(String push_url) {
		this.push_url = push_url;
	}

	
}
