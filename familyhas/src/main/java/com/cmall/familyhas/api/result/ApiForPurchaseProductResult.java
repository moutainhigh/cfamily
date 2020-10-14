package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;
import com.cmall.familyhas.model.OrderDetail;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForPurchaseProductResult extends RootResult {

	@ZapcomApi(value = "详细订单信息")
	private List<OrderDetail> orderDetailList =  new ArrayList<OrderDetail>();

	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
	
	
}
