package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiDLQAddToolsInput extends RootInput{ 
	
	@ZapcomApi(value="操作类型",require= 1,remark="1001:添加信息;1002:设置排序;1003:获取商品信息;1004:添加广告;1005:批量删除轮播广告;1006:大陆桥（发布/取消发布）;1007:大陆桥专题页修改 提交;")
	private String paramType = "";
	
	@ZapcomApi(value = "栏目名称")
	private String programa_name = "";
	
	@ZapcomApi(value = "对应英文")
	private String programa_english = ""; 
	
	@ZapcomApi(value = "食材名")
	private String food_name = "";
	
	@ZapcomApi(value = "份量")
	private String weight = "";
	
	@ZapcomApi(value = "商品编号")
	private String common_number = "";
	
	@ZapcomApi(value = "图片")
	private String picture = "";
	
	@ZapcomApi(value = "位置")
	private String location = "";
	
	@ZapcomApi(value = "描述")
	private String describe = "";
	
	@ZapcomApi(value = "栏目类别")
	private String id_number = "";
	
	@ZapcomApi(value = "删除状态")
	private String delete_state = "";
	
	@ZapcomApi(value = "页面编号")
	private String page_number = "";
	
	@ZapcomApi(value = "TV渠道编号")
	private String tv_number = "";
	
	@ZapcomApi(value="排序")
	private String sortByUid = "";
	
	@ZapcomApi(value="广告图")
	private String gg_pic = "";
	
	@ZapcomApi(value="连接地址")
	private String gg_url = "";
	
	@ZapcomApi(value="开始时间")
	private String gg_start_time = "";
	
	@ZapcomApi(value="结束时间")
	private String gg_end_time = "";
	
	@ZapcomApi(value="uid",remark="多个用 “,” 号拼接")
	private String uid_str = "";
	
	@ZapcomApi(value="发布状态",remark="对应大陆桥专题发布状态")
	private String editStatus = "";
	
	@ZapcomApi(value="专题名称")
	private String special_name = "";
	
	@ZapcomApi(value="菜系名称")
	private String cuisine_name = "";
	
	@ZapcomApi(value="菜系图片")
	private String cuisine_picture = "";
	
	@ZapcomApi(value="视频地址")
	private String url = "";
	
	@ZapcomApi(value="优惠券活动编号")
	private String activity_code = "";
	
	@ZapcomApi(value="是否分享")
	private String is_share = "";
	
	@ZapcomApi(value="分享标题")
	private String share_title = "";
	
	@ZapcomApi(value="分享图片")
	private String share_img = "";
	
	@ZapcomApi(value="分享内容")
	private String share_content = "";
	
	@ZapcomApi(value="标识")
	private String mark = "";
	
	@ZapcomApi(value="栏目描述",remark="除大陆桥之外的渠道选填项")
	private String column_desc = "";
	
	@ZapcomApi(value="本期介绍",remark="除大陆桥之外的渠道选填项")
	private String t_describ = "";
	
	@ZapcomApi(value="渠道号",remark="区分大陆桥和其他渠道")
	private String cls_num = "";
	
	@ZapcomApi(value="排序")
	private String page_sort = "";
	
	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getPrograma_name() {
		return programa_name;
	}

	public void setPrograma_name(String programa_name) {
		this.programa_name = programa_name;
	}

	public String getPrograma_english() {
		return programa_english;
	}

	public void setPrograma_english(String programa_english) {
		this.programa_english = programa_english;
	}

	public String getFood_name() {
		return food_name;
	}

	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCommon_number() {
		return common_number;
	}

	public void setCommon_number(String common_number) {
		this.common_number = common_number;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getDelete_state() {
		return delete_state;
	}

	public void setDelete_state(String delete_state) {
		this.delete_state = delete_state;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

	public String getSortByUid() {
		return sortByUid;
	}

	public void setSortByUid(String sortByUid) {
		this.sortByUid = sortByUid;
	}

	public String getGg_pic() {
		return gg_pic;
	}

	public void setGg_pic(String gg_pic) {
		this.gg_pic = gg_pic;
	}

	public String getGg_url() {
		return gg_url;
	}

	public void setGg_url(String gg_url) {
		this.gg_url = gg_url;
	}

	public String getGg_start_time() {
		return gg_start_time;
	}

	public void setGg_start_time(String gg_start_time) {
		this.gg_start_time = gg_start_time;
	}

	public String getGg_end_time() {
		return gg_end_time;
	}

	public void setGg_end_time(String gg_end_time) {
		this.gg_end_time = gg_end_time;
	}

	public String getUid_str() {
		return uid_str;
	}

	public void setUid_str(String uid_str) {
		this.uid_str = uid_str;
	}

	public String getEditStatus() {
		return editStatus;
	}

	public void setEditStatus(String editStatus) {
		this.editStatus = editStatus;
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

	public String getCuisine_picture() {
		return cuisine_picture;
	}

	public void setCuisine_picture(String cuisine_picture) {
		this.cuisine_picture = cuisine_picture;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTv_number() {
		return tv_number;
	}

	public void setTv_number(String tv_number) {
		this.tv_number = tv_number;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
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

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getIs_share() {
		return is_share;
	}

	public void setIs_share(String is_share) {
		this.is_share = is_share;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getColumn_desc() {
		return column_desc;
	}

	public void setColumn_desc(String column_desc) {
		this.column_desc = column_desc;
	}

	public String getT_describ() {
		return t_describ;
	}

	public void setT_describ(String t_describ) {
		this.t_describ = t_describ;
	}

	public String getCls_num() {
		return cls_num;
	}

	public void setCls_num(String cls_num) {
		this.cls_num = cls_num;
	}

	public String getPage_sort() {
		return page_sort;
	}

	public void setPage_sort(String page_sort) {
		this.page_sort = page_sort;
	}
	
	
	
}
