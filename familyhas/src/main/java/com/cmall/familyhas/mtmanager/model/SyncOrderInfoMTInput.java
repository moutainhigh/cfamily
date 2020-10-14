package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 订单同步
 * @author jlin
 *
 */
public class SyncOrderInfoMTInput extends RootInput {

	@ZapcomApi(value="开始时间",require=1,demo="2014-01-20 10:25:22",verify = "minlength=19")
	private String start_time="";
	
	@ZapcomApi(value="结束时间",require=1,demo="2014-01-20 10:25:22",verify = "minlength=19")
	private String end_time="";

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	
}
