package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.BuyerShowEvaluation;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetBuyerShowEvaListResult extends RootResult {

	@ZapcomApi(value="单个买家秀评论分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value="单个买家秀评价量",remark="")
	private String evaluationNum = "0";
	
	@ZapcomApi(value = "单个买家秀评论List")
	private List<BuyerShowEvaluation> buyerShowList = new ArrayList<BuyerShowEvaluation>();

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getEvaluationNum() {
		return evaluationNum;
	}

	public void setEvaluationNum(String evaluationNum) {
		this.evaluationNum = evaluationNum;
	}

	public List<BuyerShowEvaluation> getBuyerShowList() {
		return buyerShowList;
	}

	public void setBuyerShowList(List<BuyerShowEvaluation> buyerShowList) {
		this.buyerShowList = buyerShowList;
	}
	
}
