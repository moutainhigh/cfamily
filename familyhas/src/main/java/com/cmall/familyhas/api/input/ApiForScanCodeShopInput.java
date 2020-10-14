package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForScanCodeShopInput  extends RootInput{

	@ZapcomApi(value = "频道", remark = "1000017:二台 1000026:天鹅")
	private String so_id = "";

	public String getSo_id() {
		return so_id;
	}

	public void setSo_id(String so_id) {
		this.so_id = so_id;
	}
	
}
