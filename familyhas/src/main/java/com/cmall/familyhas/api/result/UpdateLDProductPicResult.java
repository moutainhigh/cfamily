package com.cmall.familyhas.api.result;

import java.util.List;

import com.cmall.familyhas.api.model.ProductPicModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class UpdateLDProductPicResult extends RootResult {
	@ZapcomApi(value="商品编码",remark = "可为空", demo = "")
	private List<ProductPicModel> ppmodels;

	public List<ProductPicModel> getPpmodels() {
		return ppmodels;
	}

	public void setPpmodels(List<ProductPicModel> ppmodels) {
		this.ppmodels = ppmodels;
	}
	
	
}
