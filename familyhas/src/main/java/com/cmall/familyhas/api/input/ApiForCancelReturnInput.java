package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForCancelReturnInput extends RootInput {
	@ZapcomApi(value = "订单号", remark = "售后单号", require = 1, demo = "RTG140122100001")
	private String afterSaleCode = "";

	@ZapcomApi(value = "取消原因", remark = "取消原因,具体查看取消原因接口", require = 0)
	private String reason = "";

	

	public String getAfterSaleCode() {
		return afterSaleCode;
	}

	public void setAfterSaleCode(String afterSaleCode) {
		this.afterSaleCode = afterSaleCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
