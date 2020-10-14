package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetBuyerShowEvaListInput extends RootInput {

	@ZapcomApi(value="分页页码",remark="第几页数据",require=1)
	private int page = 1;
	
	@ZapcomApi(value="买家秀uid",remark="")
	private String buyerShowUid = "";

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getBuyerShowUid() {
		return buyerShowUid;
	}

	public void setBuyerShowUid(String buyerShowUid) {
		this.buyerShowUid = buyerShowUid;
	}
	
}
