package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetContentByColumnTypeInput extends RootInput {

	@ZapcomApi(value="分页页码",remark="第几页数据",require=1)
	private int page = 1;
	
	@ZapcomApi(value="栏目类型",remark="栏目类型编号")
	private String columnType = "";
	
	@ZapcomApi(value = "栏目ID",require=1)
	private String columnID = "";
	
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "";

	public String getColumnID() {
		return columnID;
	}

	public void setColumnID(String columnID) {
		this.columnID = columnID;
	}

	public String getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
}
