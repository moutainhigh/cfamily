package com.cmall.familyhas.api.video.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetVideoListPersonalInput extends RootInput {

	@ZapcomApi(value="用户编号",require=1)
	private String userCode="";
	
	@ZapcomApi(value="分页页码",remark="第几页数据",require=1)
	private int page = 1;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	
}
	