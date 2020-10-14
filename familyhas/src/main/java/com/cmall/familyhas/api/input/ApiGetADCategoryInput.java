package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetADCategoryInput extends RootInput {
	
	@ZapcomApi(value = "栏目类别编号", demo = "467703130008000100060001", require = 1)
	public String category_code = "";

	public String getCategory_code() {
		return category_code;
	}

	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	
	
}
