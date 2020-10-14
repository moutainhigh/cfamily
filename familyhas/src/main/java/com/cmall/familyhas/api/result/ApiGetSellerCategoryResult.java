package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.SellerCategory;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetSellerCategoryResult extends RootResult {
	
	@ZapcomApi(value="分类")
	private List<SellerCategory> category = new ArrayList<SellerCategory>();

	public List<SellerCategory> getCategory() {
		return category;
	}

	public void setCategory(List<SellerCategory> category) {
		this.category = category;
	}
	
}
