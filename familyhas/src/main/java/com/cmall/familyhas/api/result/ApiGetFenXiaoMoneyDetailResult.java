package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetFenXiaoMoneyDetailResult extends RootResultWeb {
	@ZapcomApi(value="退款明细集合")
	List<FXDetail> fxDetailList = new ArrayList<FXDetail>();
	@ZapcomApi(value="总页数")
	int totalPage = 0;
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<FXDetail> getFxDetailList() {
		return fxDetailList;
	}

	public void setFxDetailList(List<FXDetail> fxDetailList) {
		this.fxDetailList = fxDetailList;
	}

}
