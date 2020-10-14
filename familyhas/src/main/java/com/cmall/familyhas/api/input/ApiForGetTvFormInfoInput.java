package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetTvFormInfoInput extends RootInput {

	@ZapcomApi(value = "日期", remark = "可为空，默认查询当日数据", demo = "2014-08-08")
	private String date = "";

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
