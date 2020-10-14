package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForTodayHotSaleInput extends RootInput {

	@ZapcomApi(value="当前页码",remark="默认展示第一页，从1开始")
	private int pageIndex = 1;

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
}
