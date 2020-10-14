package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @Author fufu
* @Time 2020-8-28 14:36:04 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiRecommendForLdPaySuccessInput extends RootInput {
	@ZapcomApi(value="当前页码", remark="默认展示第一页")
	private int pageIndex = 1;
	@ZapcomApi(value="每页展示商品数", remark="默认展示每页展示10个商品")
	private int pageSize = 10;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
