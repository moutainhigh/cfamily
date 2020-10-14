package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumnPc;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiColumnDetailResult extends RootResult {

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
}