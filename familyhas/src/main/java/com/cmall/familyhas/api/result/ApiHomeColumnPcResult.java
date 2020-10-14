package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumnPc;
import com.cmall.familyhas.model.TopThreeColumn;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;


public class ApiHomeColumnPcResult extends RootResult {

	@ZapcomApi(value="前三个栏目对象")
	private TopThreeColumn topThreeColumn = new TopThreeColumn();
	
	@ZapcomApi(value="其他栏目List")
	private List<HomeColumnPc> columnList = new ArrayList<HomeColumnPc>();
	
	@ZapcomApi(value = "服务器时间")
	private String sysTime = DateUtil.getSysDateTimeString();
	
	public List<HomeColumnPc> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<HomeColumnPc> columnList) {
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
}