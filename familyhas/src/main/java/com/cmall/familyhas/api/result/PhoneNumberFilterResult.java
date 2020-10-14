package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class PhoneNumberFilterResult extends RootResult {
	
	@ZapcomApi(value="低于比较版本的手机号    用,拼接",require=1)
	private String lowNums = "";
	
	@ZapcomApi(value="高于比较版本的手机号    用,拼接",require=1)
	private String highNums = "";

	public String getLowNums() {
		return lowNums;
	}

	public void setLowNums(String lowNums) {
		this.lowNums = lowNums;
	}

	public String getHighNums() {
		return highNums;
	}

	public void setHighNums(String highNums) {
		this.highNums = highNums;
	}
	
	
}