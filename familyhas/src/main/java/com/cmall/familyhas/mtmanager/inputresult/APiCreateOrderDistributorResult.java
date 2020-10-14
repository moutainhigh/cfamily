package com.cmall.familyhas.mtmanager.inputresult;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.mtmanager.model.MTOrder;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiCreateOrderDistributorResult extends RootResultWeb {
	@ZapcomApi(value="订单商品信息",remark="订单商品信息")
	private List<MTOrder> order = new ArrayList<MTOrder>();

	public List<MTOrder> getOrder() {
		return order;
	}

	public void setOrder(List<MTOrder> order) {
		this.order = order;
	}

}