package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.EvaProduct;
import com.cmall.familyhas.api.model.ProdEvaluation;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiProdGoodEvaluationInfoResult extends RootResult {

	@ZapcomApi(value="评价商品",remark="评价商品信息")
	private EvaProduct evaProduct = new EvaProduct();
	
	@ZapcomApi(value = "商品评价内容List",remark="评价内容和图片/视频")
	private List<ProdEvaluation> evaluationList = new ArrayList<ProdEvaluation>();
	
	@ZapcomApi(value="评价图片总数")
	private int imgCount = 0;

	public int getImgCount() {
		return imgCount;
	}

	public void setImgCount(int imgCount) {
		this.imgCount = imgCount;
	}

	public EvaProduct getEvaProduct() {
		return evaProduct;
	}

	public void setEvaProduct(EvaProduct evaProduct) {
		this.evaProduct = evaProduct;
	}

	public List<ProdEvaluation> getEvaluationList() {
		return evaluationList;
	}

	public void setEvaluationList(List<ProdEvaluation> evaluationList) {
		this.evaluationList = evaluationList;
	}
	
}
