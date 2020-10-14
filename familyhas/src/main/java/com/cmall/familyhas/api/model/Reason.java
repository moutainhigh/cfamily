package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 取消发货/退款/退货/换货  原因
 * @author jlin
 *
 */
public class Reason {

	@ZapcomApi(value = "原因编码", require = 1, demo = "RGR160308100002")
	private String reason_code = "";

	@ZapcomApi(value = "原因内容", require = 1, demo = "协商一致退款")
	private String reson_content = "";

	public String getReason_code() {
		return reason_code;
	}

	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}

	public String getReson_content() {
		return reson_content;
	}

	public void setReson_content(String reson_content) {
		this.reson_content = reson_content;
	}

	public Reason(String reason_code, String reson_content) {
		super();
		this.reason_code = reason_code;
		this.reson_content = reson_content;
	}

	public Reason() {
		super();
	}

	
}
