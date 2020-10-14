package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 
 * @author fq
 *
 */
public class DLQpicListModel {

	@ZapcomApi(value="菜系名称")
	private String title = "";
	
	@ZapcomApi(value="图片连接")
	private String pic_link = "";
	
	@ZapcomApi(value="编号")
	private String page_number = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic_link() {
		return pic_link;
	}

	public void setPic_link(String pic_link) {
		this.pic_link = pic_link;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}
	
	
	
}
