package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Customer{
	
	public Customer() {
	}
	
	public Customer(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	
	@ZapcomApi(value="1:在线客服  2: 电话客服 3: 微信客服")
	private String type = "";     // 1:在线客服， 2：电话客服 3： 微信客服
	
	@ZapcomApi(value="1:在线客服(返回空) 2：电话客服 3： 微信客服(字符串)")
	private String content = "";		//在线客服为空，电话客服返回电话号码，微信客服根据需求返回具体内容
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
