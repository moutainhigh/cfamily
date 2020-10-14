package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HbOrderDetailInfo {
	
	@ZapcomApi(value = "订单编号")
	private String order_code = "";
	
	@ZapcomApi(value = "订单状态",remark="下单成功-未付款：4497153900010001，下单成功-未发货：4497153900010002，已发货：4497153900010003，已收货：4497153900010004，交易成功：4497153900010005，交易失败：4497153900010006，等待审核：4497153900010008")
	private String order_status = "";
	
	@ZapcomApi(value = "商品名称")
	private String product_name = "";
	
	@ZapcomApi(value = "下单时间")
	private String order_time = "";
	
	@ZapcomApi(value = "显示售价")
	private String show_price = "";
	
	@ZapcomApi(value = "数量")
	private String sku_num = "";
	
	@ZapcomApi(value = "订单来源",remark="推广收益: 4497471600610001,买家秀:4497471600610002")
	private String tgz_type = "";
	
	@ZapcomApi(value = "颜色")
	private String color = "";
	
	@ZapcomApi(value = "款式")
	private String style = "";
	
	@ZapcomApi(value = "推广收益")
	private String tgz_money = "";
	
	@ZapcomApi(value = "实付款")
	private String due_money = "";

	
	
	
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getShow_price() {
		return show_price;
	}

	public void setShow_price(String show_price) {
		this.show_price = show_price;
	}

	public String getTgz_money() {
		return tgz_money;
	}

	public void setTgz_money(String tgz_money) {
		this.tgz_money = tgz_money;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getSku_num() {
		return sku_num;
	}

	public void setSku_num(String sku_num) {
		this.sku_num = sku_num;
	}


	public String getTgz_type() {
		return tgz_type;
	}

	public void setTgz_type(String tgz_type) {
		this.tgz_type = tgz_type;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDue_money() {
		return due_money;
	}

	public void setDue_money(String due_money) {
		this.due_money = due_money;
	}
	
	
}
