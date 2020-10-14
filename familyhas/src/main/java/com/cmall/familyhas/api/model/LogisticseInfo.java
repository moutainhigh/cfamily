package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class LogisticseInfo {
	@ZapcomApi(value = "快递公司名称", require = 1, demo = "圆通速递")
	private String logisticse_name = "";

	@ZapcomApi(value = "快递公司编号", require = 1, demo = "yuantong")
	private String logisticse_code = "";

	public LogisticseInfo(String logisticse_name, String logisticse_code) {
		super();
		this.logisticse_name = logisticse_name;
		this.logisticse_code = logisticse_code;
	}

	public LogisticseInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLogisticse_name() {
		return logisticse_name;
	}

	public void setLogisticse_name(String logisticse_name) {
		this.logisticse_name = logisticse_name;
	}

	public String getLogisticse_code() {
		return logisticse_code;
	}

	public void setLogisticse_code(String logisticse_code) {
		this.logisticse_code = logisticse_code;
	}

}
