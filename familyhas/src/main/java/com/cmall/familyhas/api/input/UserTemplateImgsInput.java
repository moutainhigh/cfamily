package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class UserTemplateImgsInput extends RootInput {
	@ZapcomApi(value="图片编号")
	private String img_id = "";
	@ZapcomApi(value="图片排序号")
	private String img_sort = "";
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getImg_sort() {
		return img_sort;
	}
	public void setImg_sort(String img_sort) {
		this.img_sort = img_sort;
	}
	
}
