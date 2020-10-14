package com.cmall.familyhas.api.model;

import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class QaTypeModel {

	@ZapcomApi(value="问题类型编号")
	private String typeCode;
	
	@ZapcomApi(value="问题类型标题")
	private String typeTitle;
	
	@ZapcomApi(value="问题")
	private List<QaModel> questions;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeTitle() {
		return typeTitle;
	}

	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}

	public List<QaModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QaModel> questions) {
		this.questions = questions;
	}
	
}
