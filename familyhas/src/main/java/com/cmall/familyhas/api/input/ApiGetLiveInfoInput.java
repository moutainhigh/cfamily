package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetLiveInfoInput extends RootInput {
	@ZapcomApi(value = "第几页",remark = "输入页码，,从1开始为第一页" ,demo= "1",require = 1)
	private int pageNum;
	@ZapcomApi(value = "每页数量",remark = "每页数量" ,demo= "10",require = 1)
	private int pageSize;
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
