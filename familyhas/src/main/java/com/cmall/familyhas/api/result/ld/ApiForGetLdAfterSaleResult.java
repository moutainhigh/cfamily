package com.cmall.familyhas.api.result.ld;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetLdAfterSaleResult extends RootResult{
	@ZapcomApi(value="售后单列表",remark="售后单列表")
	private List<AfterSaleOrder> afterSaleOrderList = new ArrayList<AfterSaleOrder>();

	public List<AfterSaleOrder> getAfterSaleOrderList() {
		return afterSaleOrderList;
	}

	public void setAfterSaleOrderList(List<AfterSaleOrder> afterSaleOrderList) {
		this.afterSaleOrderList = afterSaleOrderList;
	}
	
}
