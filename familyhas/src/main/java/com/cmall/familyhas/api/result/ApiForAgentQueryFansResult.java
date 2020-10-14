package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 查询分销订单输出类
 */
public class ApiForAgentQueryFansResult extends RootResult {
	
	@ZapcomApi(value="总页数")
	private int countPage = 0;
	
	@ZapcomApi(value="当前页数")
	private int nowPage = 0;
	
	@ZapcomApi(value="粉丝信息")
	private List<ApiForAgentQueryFansInfo> fansList = new ArrayList<ApiForAgentQueryFansInfo>();

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public List<ApiForAgentQueryFansInfo> getFansList() {
		return fansList;
	}

	public void setFansList(List<ApiForAgentQueryFansInfo> fansList) {
		this.fansList = fansList;
	}

}
