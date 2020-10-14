package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class OrderCancelInfo {
	@ZapcomApi(value = "说明介绍")
	private String message;
	@ZapcomApi(value = "时间")
	private String time;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "OrderCancelInfo [message=" + message + ", time=" + time + "]";
	}
	
	
	

}
