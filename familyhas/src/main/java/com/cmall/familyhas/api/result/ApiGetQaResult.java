package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetQaResult extends RootResult {

	@ZapcomApi(value="问题类型")
	private String qaType;
	
	@ZapcomApi(value="问题编号")
	private String qaCode;
	
	@ZapcomApi(value="问题标题")
	private String qaTitle;
	
	@ZapcomApi(value="问题内容")
	private String qaContent;

	public String getQaType() {
		return qaType;
	}

	public void setQaType(String qaType) {
		this.qaType = qaType;
	}

	public String getQaCode() {
		return qaCode;
	}

	public void setQaCode(String qaCode) {
		this.qaCode = qaCode;
	}

	public String getQaTitle() {
		return qaTitle;
	}

	public void setQaTitle(String qaTitle) {
		this.qaTitle = qaTitle;
	}

	public String getQaContent() {
		return qaContent;
	}

	public void setQaContent(String qaContent) {
		this.qaContent = qaContent;
	}
	
}
