package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.model.TopThreeColumn;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.HomeRecommend;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeColumnResult extends RootResult {


	@ZapcomApi(value="前三个栏目对象")
	private TopThreeColumn topThreeColumn = new TopThreeColumn();
	
	@ZapcomApi(value="其他栏目List")
	private List<HomeColumn> columnList = new ArrayList<HomeColumn>();

	@ZapcomApi(value = "服务器时间")
	private String sysTime = DateUtil.getSysDateTimeString();
	
	
	@ZapcomApi(value = "首页推荐分类")
	private List<HomeRecommend> homeRecommendList = new ArrayList<HomeRecommend>();
	
	public List<HomeColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<HomeColumn> columnList) {
		this.columnList = columnList;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public TopThreeColumn getTopThreeColumn() {
		return topThreeColumn;
	}

	public void setTopThreeColumn(TopThreeColumn topThreeColumn) {
		this.topThreeColumn = topThreeColumn;
	}

	public List<HomeRecommend> getHomeRecommendList() {
		return homeRecommendList;
	}

	public void setHomeRecommendList(List<HomeRecommend> homeRecommendList) {
		this.homeRecommendList = homeRecommendList;
	}


	
}