package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.PageResults;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiNcStaffAddressResult extends RootResultWeb{

	@ZapcomApi(value="收货人信息")
	private List<ApiFamilyConsigneeAddressSelectListResult> apiFamilyConsigneeAddressSelectListResult = new ArrayList<ApiFamilyConsigneeAddressSelectListResult>();

	@ZapcomApi(value="总页数")
	private int countPage;
	@ZapcomApi(value="当前页数")
	private int nowPage;

	public List<ApiFamilyConsigneeAddressSelectListResult> getApiFamilyConsigneeAddressSelectListResult() {
		return apiFamilyConsigneeAddressSelectListResult;
	}

	public void setApiFamilyConsigneeAddressSelectListResult(
			List<ApiFamilyConsigneeAddressSelectListResult> apiFamilyConsigneeAddressSelectListResult) {
		this.apiFamilyConsigneeAddressSelectListResult = apiFamilyConsigneeAddressSelectListResult;
	}

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

	
}
