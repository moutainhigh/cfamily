package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductPunishModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class OrderCodeSearchInfoResult extends RootResult{
	@ZapcomApi(value="orderTime",remark="订单时间")
	private String orderTime = "";
	
	@ZapcomApi(value="productCode",remark="商品编号")
	private List<ProductPunishModel> list = new ArrayList<>();

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public List<ProductPunishModel> getList() {
		return list;
	}

	public void setList(List<ProductPunishModel> list) {
		this.list = list;
	}
	
	
	
}
