package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiSynchCustIdSinceCouponInput extends RootInput{
	@ZapcomApi(value="优惠券创建时间的开始时间",require=1)
	private String begin_time="";
	@ZapcomApi(value="优惠券创建时间的结束时间",require=1)
	private String end_time="";
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
}
