package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 大陆桥专题内容
 * @author zw
 *
 */
public class DLQcontent {
	
	
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
	
	@ZapcomApi(value = "图片",remark="各栏目维护图片（注：赞助商的所有图片连接用“|” 拼接）")
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
	
	@ZapcomApi(value="商品名称")
	private String product_name = "";
	
	@ZapcomApi(value="市场价")
	private String mark_price = "0";

	@ZapcomApi(value="销售价")
	private String sell_price = "0";
	
	@ZapcomApi(value="商品状态",remark="1:上架;0:下架")
	private String product_status = "";
	
	@ZapcomApi(value="库存")
	private String sales_num = "";
	
	@ZapcomApi(value="优惠券活动编码")
	private String activity_code = "";
	
	@ZapcomApi(value="栏目描述")
	private String column_descb = "";
	
	@ZapcomApi(value="本期描述")
	private String t_descb = "";
	
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

	public String getMark_price() {
		return mark_price;
	}

	public void setMark_price(String mark_price) {
		this.mark_price = mark_price;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_status() {
		return product_status;
	}

	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}

	public String getSales_num() {
		return sales_num;
	}

	public void setSales_num(String sales_num) {
		this.sales_num = sales_num;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getColumn_descb() {
		return column_descb;
	}

	public void setColumn_descb(String column_descb) {
		this.column_descb = column_descb;
	}

	public String getT_descb() {
		return t_descb;
	}

	public void setT_descb(String t_descb) {
		this.t_descb = t_descb;
	}
	
	
}
