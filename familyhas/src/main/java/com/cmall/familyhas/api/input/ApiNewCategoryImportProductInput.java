package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiNewCategoryImportProductInput extends RootInput {

	//上传excel文件名(先传到文件服务器)
	private String upload_show;
	//分类编号
	private String category_code;
	public String getUpload_show() {
		return upload_show;
	}
	public void setUpload_show(String upload_show) {
		this.upload_show = upload_show;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
}
