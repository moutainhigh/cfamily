package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 用户相册详情
 * @author 张圣瑞
 *
 */
public class ApiAlbumDetailsResult extends RootResultWeb {
	@ZapcomApi(value="模板编号",remark="模板编号")
	private int template_id = 0;
	@ZapcomApi(value="模板标题",remark="模板标题")
	private String template_title = "";
	@ZapcomApi(value="开场标语",remark="开场标语")
	private String open_title = "";
	@ZapcomApi(value="最大上传数量",remark="最大上传数量")
	private int max_up_imgs = 0;
	@ZapcomApi(value="分享标题",remark="分享标题")
	private String share_title = "";
	@ZapcomApi(value="分享图片",remark="分享图片")
	private String share_img = "";
	@ZapcomApi(value="相册背景音乐",demo="0",remark="0:为默认模板音乐")
	private int bmgId = 0;
	@ZapcomApi(value="相册图片",remark="相册图片")
	private List<UserTemplateImgsResult> user_template_imgs  = new ArrayList<UserTemplateImgsResult>();
	public int getBmgId() {
		return bmgId;
	}
	public void setBmgId(int bmgId) {
		this.bmgId = bmgId;
	}
	public int getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(int template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_title() {
		return template_title;
	}
	public void setTemplate_title(String template_title) {
		this.template_title = template_title;
	}
	public String getOpen_title() {
		return open_title;
	}
	public void setOpen_title(String open_title) {
		this.open_title = open_title;
	}
	public int getMax_up_imgs() {
		return max_up_imgs;
	}
	public void setMax_up_imgs(int max_up_imgs) {
		this.max_up_imgs = max_up_imgs;
	}
	public String getShare_title() {
		return share_title;
	}
	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}
	public String getShare_img() {
		return share_img;
	}
	public void setShare_img(String share_img) {
		this.share_img = share_img;
	}
	public List<UserTemplateImgsResult> getUser_template_imgs() {
		return user_template_imgs;
	}
	public void setUser_template_imgs(List<UserTemplateImgsResult> user_template_imgs) {
		this.user_template_imgs = user_template_imgs;
	}
	
}
