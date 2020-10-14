package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.SellerCategorySeInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiForCategoryResult extends RootResult {

	@ZapcomApi(value = "二级类目List", remark="内含有三级类目List" ,demo="")
	private List<SellerCategorySeInfo> scs= new ArrayList<SellerCategorySeInfo>();

	public List<SellerCategorySeInfo> getScs() {
		return scs;
	}

	public void setScs(List<SellerCategorySeInfo> scs) {
		this.scs = scs;
	}
	
}