package com.cmall.familyhas.api.result;


import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HbTxDetailInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiHbTxDetailsResult extends RootResultWeb {
	@ZapcomApi(value = "惠币提现明细信息")
	private List<HbTxDetailInfo> hbs = new ArrayList<HbTxDetailInfo>();
	
	@ZapcomApi(value = "总页数")
	private String totalPage = "";
	
	@ZapcomApi(value = "当前页")
	private String nowPage = "";

	
	
	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getNowPage() {
		return nowPage;
	}

	public void setNowPage(String nowPage) {
		this.nowPage = nowPage;
	}

	public List<HbTxDetailInfo> getHbs() {
		return hbs;
	}

	public void setHbs(List<HbTxDetailInfo> hbs) {
		this.hbs = hbs;
	}
	
	
}
