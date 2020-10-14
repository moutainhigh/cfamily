package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForNoticeInput extends RootInput {

	@ZapcomApi(value = "显示位置", remark = "默认查询全部的通知<br>4497477800010001-申请售后页", require = 0, demo = "4497477800010001")
	private String notice_show_place = "";

	public String getNotice_show_place() {
		return notice_show_place;
	}

	public void setNotice_show_place(String notice_show_place) {
		this.notice_show_place = notice_show_place;
	}

	
}
