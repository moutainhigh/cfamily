package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetStoreDistrictNewInput extends RootInput {
	
	@ZapcomApi(value="父级编码", remark="获取省时，不需要传或传空字符串")
	private String parentCode = "";

	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
