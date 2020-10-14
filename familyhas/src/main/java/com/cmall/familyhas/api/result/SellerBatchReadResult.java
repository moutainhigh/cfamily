package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.topapi.RootResult;

public class SellerBatchReadResult extends RootResult{
	private String noReadCounts =  "-1";

	public String getNoReadCounts() {
		return noReadCounts;
	}

	public void setNoReadCounts(String noReadCounts) {
		this.noReadCounts = noReadCounts;
	}
	
}
