package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class QaModel {
	
	@ZapcomApi(value="问题编号")
	private String qa_code;

	@ZapcomApi(value="问题标题")
	private String qa_title;
	
	public String getQa_code() {
		return qa_code;
	}

	public void setQa_code(String qa_code) {
		this.qa_code = qa_code;
	}

	public String getQa_title() {
		return qa_title;
	}

	public void setQa_title(String qa_title) {
		this.qa_title = qa_title;
	}
	
}
