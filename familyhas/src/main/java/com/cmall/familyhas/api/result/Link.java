package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Link {
	@ZapcomApi(value = "功能连接对应的唯一标识", require = 1, demo = "xxxxx")
	private String linkCode = "449748240001";

	@ZapcomApi(value = "功能连接上面的文字", require = 1, demo = "xxxxx")
	private String linkTitle = "售后详情：点击查看售后详情";
	
	@ZapcomApi(value = "按钮状态",remark="1:连接展示and正常使用；2：连接展示不可使用（不可点击）;3：连接不可用（不展示）")
	private Integer linkStatus = 1;

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public String getLinkTitle() {
		return linkTitle;
	}

	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public Integer getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Integer linkStatus) {
		this.linkStatus = linkStatus;
	}
	
	
}
