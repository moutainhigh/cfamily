package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.GetBrowseHistory;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetBrowesHistoryResult extends RootResultWeb{
	@ZapcomApi(value="浏览历史list列表")
	private List<GetBrowseHistory> getBrowseHistory = new ArrayList<GetBrowseHistory>();
	
	@ZapcomApi(value="当前页数")
	private int nowPage = 0;
	
	@ZapcomApi(value="页码总数")
	private int pagination=0;

	public List<GetBrowseHistory> getGetBrowseHistory() {
		return getBrowseHistory;
	}

	public void setGetBrowseHistory(List<GetBrowseHistory> getBrowseHistory) {
		this.getBrowseHistory = getBrowseHistory;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
}
