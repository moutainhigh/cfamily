package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumnContent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetContentByColumnTypeResult extends RootResult {

	@ZapcomApi(value="分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value="其他栏目List")
	private List<HomeColumnContent> columnList = new ArrayList<HomeColumnContent>();
	
	public List<HomeColumnContent> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<HomeColumnContent> columnList) {
		this.columnList = columnList;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
