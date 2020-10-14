package com.cmall.familyhas.mtmanager.inputresult;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.mtmanager.model.OrderStatusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class SyncOrderStatusResult extends RootResult {
	@ZapcomApi(value = "订单详情实体", remark = "", demo = "")
	private List<OrderStatusModel> ListInfo = new ArrayList<OrderStatusModel>();

	public List<OrderStatusModel> getListInfo() {
		return ListInfo;
	}

	public void setListInfo(List<OrderStatusModel> listInfo) {
		ListInfo = listInfo;
	}

}
