package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiImportHJYLDProductInput extends RootInput{

	@ZapcomApi(value="上传excel文件路径")
	private String upload_excel_url = "";

	/**
	 * 返回: the upload_excel_url <br>
	 * 
	 * 时间: 2016-8-4 下午4:58:04
	 */
	public String getUpload_excel_url() {
		return upload_excel_url;
	}

	/**
	 * 参数: upload_excel_url the upload_excel_url to set <br>
	 * 
	 * 时间: 2016-8-4 下午4:58:04
	 */
	public void setUpload_excel_url(String upload_excel_url) {
		this.upload_excel_url = upload_excel_url;
	}
	
	
	
	
}
