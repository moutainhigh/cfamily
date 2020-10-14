package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FarmExchangeSku;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiFarmExchangeGoodsListResult extends RootResultWeb{

	@ZapcomApi(value = "sku列表")
	private List<FarmExchangeSku> skuList = new ArrayList<FarmExchangeSku>();

	public List<FarmExchangeSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<FarmExchangeSku> skuList) {
		this.skuList = skuList;
	}
	
}
