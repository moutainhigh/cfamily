package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeColumnNewResult extends RootResult {

	@ZapcomApi(value="其他栏目List")
	private List<HomeColumn> columnList = new ArrayList<HomeColumn>();
	
	@ZapcomApi(value = "猜你喜欢栏目显示控制", remark = "4497480100020001 显示，4497480100020002 隐藏")
	private String maybeLove;
	
	@ZapcomApi(value = "导航项编码", remark="编码从首页导航栏接口获取" )
	private String navCode = "";
	
	@ZapcomApi(value = "服务器时间")
	private String sysTime = DateUtil.getSysDateTimeString();



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

	public String getMaybeLove() {
		return maybeLove;
	}

	public void setMaybeLove(String maybeLove) {
		this.maybeLove = maybeLove;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	
}