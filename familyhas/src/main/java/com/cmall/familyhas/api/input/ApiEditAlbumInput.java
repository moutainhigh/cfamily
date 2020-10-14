package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiEditAlbumInput extends RootInput {
	@ZapcomApi(value="相册编号")
	private String album_id = "";
	@ZapcomApi(value="开场标语")
	private String open_title = "";
	@ZapcomApi(value="模板编号")
	private String template_id = "";
	@ZapcomApi(value="相册详情")
	private List<UserTemplateImgsInput> user_template_imgs = new ArrayList<UserTemplateImgsInput>();
	@ZapcomApi(value="相册背景音乐",demo="0",remark="0:为默认模板音乐")
	private int bmgId = 0;
	@ZapcomApi(value="openId")
	private String openId = "";
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getBmgId() {
		return bmgId;
	}
	public void setBmgId(int bmgId) {
		this.bmgId = bmgId;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getOpen_title() {
		return open_title;
	}
	public void setOpen_title(String open_title) {
		this.open_title = open_title;
	}
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public List<UserTemplateImgsInput> getUser_template_imgs() {
		return user_template_imgs;
	}
	public void setUser_template_imgs(List<UserTemplateImgsInput> user_template_imgs) {
		this.user_template_imgs = user_template_imgs;
	}
	
}
