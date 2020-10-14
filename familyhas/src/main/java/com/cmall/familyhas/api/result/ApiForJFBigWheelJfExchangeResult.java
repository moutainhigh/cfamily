package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForJFBigWheelJfExchangeResult extends RootResult {

	@ZapcomApi(value = "积分兑换次数是否成功")
	private String isSuccess = "N";
	
	@ZapcomApi(value = "剩余抽奖次数")
	private int remainDrawNum = 0;
	
	@ZapcomApi(value = "用户剩余积分")
	private int integralTotal = 0;

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}

	public int getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(int integralTotal) {
		this.integralTotal = integralTotal;
	}

	
}
