package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class AddCommentModel {

	@ZapcomApi(value="sku编号")
	private String sku_code = "" ;
	
	@ZapcomApi(value="商品编号")
	private String product_code = "" ;
	
	@ZapcomApi(value="评论内容")
	private String comment_content = "";
	
	@ZapcomApi(value="订单编号")
	private String order_code = "";
	
	@ZapcomApi(value="评分")
	private String grade  = "";
	
	@ZapcomApi(value="评论图片")
	private List<String> comment_photo = new ArrayList<String>();
	
	@ZapcomApi(value="ccvid视频id",remark="评价小视频id",demo="")
	private String ccvid  = "";
	
	
	public String getCcvid() {
		return ccvid;
	}

	public void setCcvid(String ccvid) {
		this.ccvid = ccvid;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<String> getComment_photo() {
		return comment_photo;
	}

	public void setComment_photo(List<String> comment_photo) {
		this.comment_photo = comment_photo;
	}
	
}
