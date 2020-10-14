package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ImagePropertyInput extends RootInput{
	
	@ZapcomApi(value="图片Url",remark="图片Url",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/990aae2a7.jpg")
	private String imageUrl = "";

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
