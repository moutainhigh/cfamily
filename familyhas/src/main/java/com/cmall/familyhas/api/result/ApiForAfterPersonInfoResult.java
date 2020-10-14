package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForAfterPersonInfoResult extends RootResultWeb {
	@ZapcomApi(value = "售后联系人", demo = "张晓敏")
	private String after_sale_person = "";
	@ZapcomApi(value = "售后联系电话", demo = "1376567298")
	private String after_sale_mobile = "";

	public String getAfter_sale_person() {
		return after_sale_person;
	}

	public void setAfter_sale_person(String after_sale_person) {
		this.after_sale_person = after_sale_person;
	}

	public String getAfter_sale_mobile() {
		return after_sale_mobile;
	}

	public void setAfter_sale_mobile(String after_sale_mobile) {
		this.after_sale_mobile = after_sale_mobile;
	}
}
