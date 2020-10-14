package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAgentQueryFansInput extends RootInput {
	
	@ZapcomApi(value = "下一页", require = 1)
	private String nextPage = "1";
	@ZapcomApi(value = "查询开始日期", remark = "2020-04-21")
	private String startDate = "";
	@ZapcomApi(value = "查询结束日期", remark = "2020-04-21")
	private String endDate = "";
	
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
