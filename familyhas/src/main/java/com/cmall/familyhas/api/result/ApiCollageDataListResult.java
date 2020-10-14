package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumnContent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiCollageDataListResult extends RootResult {
	@ZapcomApi(value="内容list")
	private List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
	@ZapcomApi(value="页数")
	private int pageNum = 0;
	
	public List<HomeColumnContent> getContentList() {
		return contentList;
	}
	public void setContentList(List<HomeColumnContent> contentList) {
		this.contentList = contentList;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
}
