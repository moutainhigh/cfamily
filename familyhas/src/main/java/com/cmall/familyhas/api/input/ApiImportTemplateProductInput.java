package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiImportTemplateProductInput extends RootInput {

	
	//上传excel文件名(先传到文件服务器)
	private String upload_show;
	//模板编号
	private String templete_number="";
	public String getUpload_show() {
		return upload_show;
	}
	public void setUpload_show(String upload_show) {
		this.upload_show = upload_show;
	}
	public String getTemplete_number() {
		return templete_number;
	}
	public void setTemplete_number(String templete_number) {
		this.templete_number = templete_number;
	}
		
}
