package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class AddressInput extends RootInput{
	@ZapcomApi(value="收货人",require=1)
	private String receive_person="";
	@ZapcomApi(value="地区编码",require=1)
	private String area_code="";
	@ZapcomApi(value="详细地址",require=1)
	private String address="";
	@ZapcomApi(value="邮政编码")
	private String postcode="";
	@ZapcomApi(value="电话",require=1,verify = "base=mobile")
	private String mobilephone="";
	@ZapcomApi(value="标记",remark="1:默认, 0不默认",verify={ "in=0,1" })
	private String flag_default="0";
	
	public String getReceive_person() {
		return receive_person;
	}

	public void setReceive_person(String receive_person) {
		this.receive_person = receive_person;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getFlag_default() {
		return flag_default;
	}

	public void setFlag_default(String flag_default) {
		this.flag_default = flag_default;
	}
	
}
