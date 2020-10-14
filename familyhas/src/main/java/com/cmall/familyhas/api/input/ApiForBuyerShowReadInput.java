package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBuyerShowReadInput extends RootInput {

	@ZapcomApi(value="买家秀uid",remark="")
	private String buyerShowUid = "";

	public String getBuyerShowUid() {
		return buyerShowUid;
	}

	public void setBuyerShowUid(String buyerShowUid) {
		this.buyerShowUid = buyerShowUid;
	}
	
}
