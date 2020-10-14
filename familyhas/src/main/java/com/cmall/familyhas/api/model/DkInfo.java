package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class DkInfo {
	@ZapcomApi(value = "积分数量")
	private String jfNum;
	
	@ZapcomApi(value = "天数")
	private int jfDay;
	
	@ZapcomApi(value = "是否打卡Y/N")
	private String dkYn;
	
	@ZapcomApi(value = "展示logo")
	private String jfLogo;

	public String getJfNum() {
		return jfNum;
	}

	public void setJfNum(String jfNum) {
		this.jfNum = jfNum;
	}

	public int getJfDay() {
		return jfDay;
	}

	public void setJfDay(int jfDay) {
		this.jfDay = jfDay;
	}

	public String getDkYn() {
		return dkYn;
	}

	public void setDkYn(String dkYn) {
		this.dkYn = dkYn;
	}

	public String getJfLogo() {
		return jfLogo;
	}

	public void setJfLogo(String jfLogo) {
		this.jfLogo = jfLogo;
	}		


}
