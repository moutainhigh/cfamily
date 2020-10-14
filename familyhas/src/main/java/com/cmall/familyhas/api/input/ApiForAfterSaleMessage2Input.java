package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAfterSaleMessage2Input extends RootInput {

	@ZapcomApi(value = "售后类型", require = 1)
	private String type = "";
	
	@ZapcomApi(value = "flag", require = 1)
	private String flag = "";

	@ZapcomApi(value = "售后联系人", require = 1)
	private String after_sale_person = "";

	@ZapcomApi(value = "售后联系电话", require = 1)
	private String after_sale_mobile = "";

	@ZapcomApi(value = "售后收货地址", require = 1)
	private String after_sale_address = "";

	@ZapcomApi(value = "售后地址邮编", require = 1)
	private String after_sale_postcode = "";
	
	@ZapcomApi(value = "订单号", require = 1)
	private String order_code = "";

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

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

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
