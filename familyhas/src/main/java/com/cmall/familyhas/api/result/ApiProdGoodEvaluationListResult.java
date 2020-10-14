package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumnContent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiProdGoodEvaluationListResult extends RootResult {

	@ZapcomApi(value="商品评价模板内容分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value = "商品评价模板内容List")
	private List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
	
	@ZapcomApi(value="首页数据展示类型",remark="商品评价:0;其他:1")
	private String homeShowType = "";

	public String getHomeShowType() {
		return homeShowType;
	}

	public void setHomeShowType(String homeShowType) {
		this.homeShowType = homeShowType;
	}

	public List<HomeColumnContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<HomeColumnContent> contentList) {
		this.contentList = contentList;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
