package com.cmall.familyhas.mtmanager.inputresult;

import com.cmall.familyhas.mtmanager.model.SkuStockPriceInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForStockPriceResult extends RootResult {

	@ZapcomApi(value = "sku实体", remark = "", demo = "")
	private SkuStockPriceInfo stockPriceInfo = new SkuStockPriceInfo();

	public SkuStockPriceInfo getStockPriceInfo() {
		return stockPriceInfo;
	}

	public void setStockPriceInfo(SkuStockPriceInfo stockPriceInfo) {
		this.stockPriceInfo = stockPriceInfo;
	}
	
}
