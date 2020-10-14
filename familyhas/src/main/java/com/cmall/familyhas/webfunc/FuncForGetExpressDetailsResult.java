package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class FuncForGetExpressDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value="物流信息列表",remark="物流流转信息")
	List<ExpressDetailsResult> list = new ArrayList<ExpressDetailsResult>();

	public List<ExpressDetailsResult> getList() {
		return list;
	}

	public void setList(List<ExpressDetailsResult> list) {
		this.list = list;
	}
	
}
