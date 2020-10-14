package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiCollageDataListInput extends RootInput {
	@ZapcomApi(value="页数", require=1)
	private int page = 1;
	@ZapcomApi(value="每页条数")
	private int pageCount = 10;
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "0";
	
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
	
	public String getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}
}
