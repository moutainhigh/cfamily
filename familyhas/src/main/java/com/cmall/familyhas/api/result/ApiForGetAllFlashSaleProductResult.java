package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetAllFlashSaleProductResult extends RootResult {

	@ZapcomApi(value="秒杀商品列表",remark="")
	private List<FlashSaleProduct> items = new ArrayList<FlashSaleProduct>();

	public List<FlashSaleProduct> getItems() {
		return items;
	}

	public void setItems(List<FlashSaleProduct> items) {
		this.items = items;
	}
	
}
