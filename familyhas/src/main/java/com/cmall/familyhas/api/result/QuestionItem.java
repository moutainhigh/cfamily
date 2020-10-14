package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class QuestionItem{
	@ZapcomApi(value="问题标题")
	private String title = "";  // 问题标题.
	
	@ZapcomApi(value=" 问题内容")
	private String content = "";  // 问题内容
	
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
}
