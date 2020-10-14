package com.cmall.familyhas.api.input.apphome;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;


public class ActivityJoinUpInput extends RootInput{
	
	@ZapcomApi(value="用户姓名",remark="参与活动人姓名",require=0)
	String name = "";
	
	@ZapcomApi(value="电话",remark="参与活动人电话",require=0)
	String phone = "";
	
	@ZapcomApi(value="身份证号",remark="参与活动人身份证号",require=0)
	String personal_num = "";
	
	@ZapcomApi(value="所在城市",remark="活动所在城市",require=0)
	String city = "";
	
	@ZapcomApi(value="活动编号",remark="所属活动编号",require=1)
	String activity_uid = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPersonal_num() {
		return personal_num;
	}

	public void setPersonal_num(String personal_num) {
		this.personal_num = personal_num;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getActivity_uid() {
		return activity_uid;
	}

	public void setActivity_uid(String activity_uid) {
		this.activity_uid = activity_uid;
	}

	
	
}
