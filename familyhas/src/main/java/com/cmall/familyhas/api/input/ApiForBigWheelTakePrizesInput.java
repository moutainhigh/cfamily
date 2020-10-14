package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBigWheelTakePrizesInput extends RootInput {

	@ZapcomApi(value = "奖品中奖编码")
	private String jpCodeSeq="";
	
	@ZapcomApi(value = "收货地址编号")
	private String addressId="";

	public String getJpCodeSeq() {
		return jpCodeSeq;
	}

	public void setJpCodeSeq(String jpCodeSeq) {
		this.jpCodeSeq = jpCodeSeq;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
}
