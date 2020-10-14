package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.MediaProductInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetProductInfoResult extends RootResult{

	@ZapcomApi(value="商品信息列表")
	List<MediaProductInfo> productInfoList = new ArrayList<MediaProductInfo>();

	public List<MediaProductInfo> getProductInfoList() {
		return productInfoList;
	}

	public void setProductInfoList(List<MediaProductInfo> productInfoList) {
		this.productInfoList = productInfoList;
	}
	
	
}
