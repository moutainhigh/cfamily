package com.cmall.familyhas.api.result;


import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HbDetailInfo;
import com.cmall.familyhas.api.model.IntegralInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiHbDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value = "是否最后页Y/N")
	private String pageFlag = "";
	
	@ZapcomApi(value = "总惠币数")
	private String hbTotal = "";
	
	@ZapcomApi(value = "快过期惠币数量")
	private String expireIntegral = "";
	
	@ZapcomApi(value = "总数量")
	private String total = "";
	
	@ZapcomApi(value = "总页数")
	private String totalPage = "";
	
	@ZapcomApi(value = "惠币明细列表")
	List<HbDetailInfo> list = new ArrayList<HbDetailInfo>();

	
	
	
	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getExpireIntegral() {
		return expireIntegral;
	}

	public void setExpireIntegral(String expireIntegral) {
		this.expireIntegral = expireIntegral;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getHbTotal() {
		return hbTotal;
	}

	public void setHbTotal(String hbTotal) {
		this.hbTotal = hbTotal;
	}

	public List<HbDetailInfo> getList() {
		return list;
	}

	public void setList(List<HbDetailInfo> list) {
		this.list = list;
	}

	
	
	
	
	
}
