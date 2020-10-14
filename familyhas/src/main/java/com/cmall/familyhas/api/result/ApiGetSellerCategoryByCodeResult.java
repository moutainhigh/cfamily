package com.cmall.familyhas.api.result;

import com.cmall.familyhas.api.model.SellerCategory;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetSellerCategoryByCodeResult extends RootResult {
	
	@ZapcomApi(value="分类")
	private SellerCategory category = new SellerCategory();

	public SellerCategory getCategory() {
		return category;
	}

	public void setCategory(SellerCategory category) {
		this.category = category;
	}

}
