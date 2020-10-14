package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.Item;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeColumnContentListResult extends RootResult {
	
	@ZapcomApi(value = "商品内容列表")
	//private List<HomeColumnContentProductInfo> contentList = new ArrayList<HomeColumnContentProductInfo>();
	private List<Item> contentList = new ArrayList<Item>();
	@ZapcomApi(value = "栏目名称")
	private String columnName = "";
	@ZapcomApi(value = "总页数")
	private int totalPage = 1;

	public List<Item> getContentList() {
		return contentList;
	}

	public void setContentList(List<Item> contentList) {
		this.contentList = contentList;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

/*	public List<HomeColumnContentProductInfo> getContentList() {
		return contentList;
	}

	public void setContentList(List<HomeColumnContentProductInfo> contentList) {
		this.contentList = contentList;
	}*/

	
}
