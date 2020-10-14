package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.SkuBaseInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


public class ApiForProductInfoResult extends RootResultWeb{
	@ZapcomApi(value = "商品基本信息")
    private   List<SkuBaseInfo> skuInfoList = new ArrayList<SkuBaseInfo>();

	/**
	 * @return the skuInfoList
	 */
	public List<SkuBaseInfo> getSkuInfoList() {
		return skuInfoList;
	}

	/**
	 * @param skuInfoList the skuInfoList to set
	 */
	public void setSkuInfoList(List<SkuBaseInfo> skuInfoList) {
		this.skuInfoList = skuInfoList;
	}
    
}
