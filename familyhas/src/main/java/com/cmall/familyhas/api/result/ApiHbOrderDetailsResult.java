package com.cmall.familyhas.api.result;


import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HbOrderDetailInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiHbOrderDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value = "订单信息列表")
	private List<HbOrderDetailInfo> list = new ArrayList<HbOrderDetailInfo>();
	

	public List<HbOrderDetailInfo> getList() {
		return list;
	}

	public void setList(List<HbOrderDetailInfo> list) {
		this.list = list;
	}
	
	
}
