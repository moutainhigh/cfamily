package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 用户上传图片详情
 * @author 张圣瑞
 *
 */
public class UserTemplateImgsResult {
	@ZapcomApi(value="图片编号",remark="图片编号")
	private String img_id = "";
	@ZapcomApi(value="图片排序号",remark="图片排序号")
	private int img_sort = 0;
	@ZapcomApi(value="缩略图",remark="缩略图")
	private String img_preview_url = "";
	@ZapcomApi(value="用户上传主图",remark="用户上传主图")
	private String img_url = "";
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public int getImg_sort() {
		return img_sort;
	}
	public void setImg_sort(int img_sort) {
		this.img_sort = img_sort;
	}
	public String getImg_preview_url() {
		return img_preview_url;
	}
	public void setImg_preview_url(String img_preview_url) {
		this.img_preview_url = img_preview_url;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
}
