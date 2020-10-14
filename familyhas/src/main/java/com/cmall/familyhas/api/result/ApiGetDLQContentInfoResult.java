package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.DLQCommentModel;
import com.cmall.familyhas.api.model.DLQcontent;
import com.cmall.familyhas.api.model.DLQpicListModel;
import com.cmall.familyhas.api.model.DLQpicture;
import com.cmall.familyhas.api.model.DLQshare;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetDLQContentInfoResult extends RootResult { 
	
	
	@ZapcomApi(value="页面编号")
	private String page_number = "";
	
	@ZapcomApi(value="专题名称")
	private String special_name = "";
	
	@ZapcomApi(value="菜系名称")
	private String cuisine_name = "";
	
	@ZapcomApi(value="菜系图片")
	private String cuisine_pic = "";
	
	@ZapcomApi(value="视频地址")
	private String url = "";

	@ZapcomApi(value="内容信息",remark="")
	private List<DLQcontent> contents = new ArrayList<DLQcontent>();
	
	@ZapcomApi(value="广告图",remark="上部/下部广告图")
	private List<DLQpicture> picList = new ArrayList<DLQpicture>();
	
	@ZapcomApi(value="分享信息")
	private DLQshare share_info = new DLQshare();
	
	@ZapcomApi(value="推荐列表")
	private List<DLQpicListModel> recomendList = new ArrayList<DLQpicListModel>();
	
	@ZapcomApi(value="电视编号")
	private String tv_number = "";
	
	@ZapcomApi(value="评论列表")
	private List<DLQCommentModel> commentList = new ArrayList<DLQCommentModel>();
	
	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

	public String getSpecial_name() {
		return special_name;
	}

	public void setSpecial_name(String special_name) {
		this.special_name = special_name;
	}

	public String getCuisine_name() {
		return cuisine_name;
	}

	public void setCuisine_name(String cuisine_name) {
		this.cuisine_name = cuisine_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<DLQcontent> getContents() {
		return contents;
	}

	public void setContents(List<DLQcontent> contents) {
		this.contents = contents;
	}

	public List<DLQpicture> getPicList() {
		return picList;
	}

	public void setPicList(List<DLQpicture> picList) {
		this.picList = picList;
	}

	public String getCuisine_pic() {
		return cuisine_pic;
	}

	public void setCuisine_pic(String cuisine_pic) {
		this.cuisine_pic = cuisine_pic;
	}

	public DLQshare getShare_info() {
		return share_info;
	}

	public void setShare_info(DLQshare share_info) {
		this.share_info = share_info;
	}

	public List<DLQpicListModel> getRecomendList() {
		return recomendList;
	}

	public void setRecomendList(List<DLQpicListModel> recomendList) {
		this.recomendList = recomendList;
	}

	public String getTv_number() {
		return tv_number;
	}

	public void setTv_number(String tv_number) {
		this.tv_number = tv_number;
	}

	public List<DLQCommentModel> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<DLQCommentModel> commentList) {
		this.commentList = commentList;
	}
	
	
	
}
