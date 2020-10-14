package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProdEvaluation {

	@ZapcomApi(value = "商品评价内容")
	private String evaluationContent = "";
	
	@ZapcomApi(value = "评价图片或者视频list")
	private List<EvaluationImg> evaluationImgList = new ArrayList<EvaluationImg>();

	public String getEvaluationContent() {
		return evaluationContent;
	}

	public void setEvaluationContent(String evaluationContent) {
		this.evaluationContent = evaluationContent;
	}

	public List<EvaluationImg> getEvaluationImgList() {
		return evaluationImgList;
	}

	public void setEvaluationImgList(List<EvaluationImg> evaluationImgList) {
		this.evaluationImgList = evaluationImgList;
	}

	
}
