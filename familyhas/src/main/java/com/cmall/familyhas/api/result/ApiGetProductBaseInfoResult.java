package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductPic;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetProductBaseInfoResult extends RootResult {
	@ZapcomApi(value="商品图片信息")
	private List<ProductPic> productPic = new ArrayList<ProductPic>();

	public List<ProductPic> getProductPic() {
		return productPic;
	}

	public void setProductPic(List<ProductPic> productPic) {
		this.productPic = productPic;
	}
	
}
