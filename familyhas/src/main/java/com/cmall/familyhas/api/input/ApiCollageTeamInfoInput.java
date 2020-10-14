package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiCollageTeamInfoInput extends RootInput {
	@ZapcomApi(value="拼团编码",require=1)
	private String collageCode = "";
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "0";

	public String getCollageCode() {
		return collageCode;
	}
	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}
	
	public String getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}
}
