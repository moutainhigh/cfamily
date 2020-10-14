package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 商户售后地址
 * @author jlin
 *
 */
public class ApiForUserAddressInfoResult extends RootResultWeb {

	@ZapcomApi(value="售后联系人",demo="xxx")
	private String after_sale_person="";
	
	@ZapcomApi(value="售后联系电话",demo="xxx")
	private String after_sale_mobile="";
	
	@ZapcomApi(value="售后收货地址",demo="xxx")
	private String after_sale_address="";
	
	@ZapcomApi(value="售后地址邮编",demo="xxx")
	private String after_sale_postcode="";

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

	public String getAfter_sale_address() {
		return after_sale_address;
	}

	public void setAfter_sale_address(String after_sale_address) {
		this.after_sale_address = after_sale_address;
	}

	public String getAfter_sale_postcode() {
		return after_sale_postcode;
	}

	public void setAfter_sale_postcode(String after_sale_postcode) {
		this.after_sale_postcode = after_sale_postcode;
	}
}
