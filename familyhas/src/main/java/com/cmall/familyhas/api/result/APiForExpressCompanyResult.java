package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.LogisticseInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiForExpressCompanyResult extends RootResult{
	@ZapcomApi(value = "公司列表", require = 1)
	private List<LogisticseInfo> companyList=new ArrayList<LogisticseInfo>();

	public List<LogisticseInfo> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<LogisticseInfo> companyList) {
		this.companyList = companyList;
	}

}
