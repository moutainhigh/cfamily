package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiVipExclusiveClassRoomInput extends RootInput {
	@ZapcomApi(value="搜索关键词")
	private String searchKey = "";
	@ZapcomApi(value="页数")
	private int page = 1;
	@ZapcomApi(value="每页条数")
	private int pageCount = 10;

	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
