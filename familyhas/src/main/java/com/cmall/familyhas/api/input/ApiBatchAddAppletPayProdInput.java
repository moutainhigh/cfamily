package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiBatchAddAppletPayProdInput extends RootInput {

	//上传excel文件名(先传到文件服务器)
	private String upload_show;

	public String getUpload_show() {
		return upload_show;
	}

	public void setUpload_show(String upload_show) {
		this.upload_show = upload_show;
	}
	
}
