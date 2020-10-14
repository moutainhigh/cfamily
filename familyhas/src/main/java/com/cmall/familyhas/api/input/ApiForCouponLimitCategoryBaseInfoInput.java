package com.cmall.familyhas.api.input;


import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCouponLimitCategoryBaseInfoInput extends RootInput{
	@ZapcomApi(value = "分类编号",require=1)
	private List<String> categoryCodeArr = new ArrayList<String>();

	@ZapcomApi(value = "所属系统",require=1)
	private String sellerCode = "";

	public List<String> getCategoryCodeArr() {
		return categoryCodeArr;
	}

	public void setCategoryCodeArr(List<String> categoryCodeArr) {
		this.categoryCodeArr = categoryCodeArr;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	
		
}
