package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 新品推荐
 * 
 * @author guz
 *
 */
public class NewProducts {
	
	@ZapcomApi(value = "图片地址",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String img_url = "";
	
	@ZapcomApi(value = "图片名称",demo="让子弹飞一会")
	private String item_name = "";
	
	@ZapcomApi(value = "商品编号",demo="8019404180")
	private String goods_num = "";
	
	@ZapcomApi(value = "商品名称",demo="男表 测试商品添加  酒红色 尺码10")
	private String goods_name = "";
	
	@ZapcomApi(value = "卖点描述",demo="好")
	private String goods_description = "";
	
	@ZapcomApi(value = "现价",demo="453.22")
	private double current_price =0;
	
	@ZapcomApi(value = "原价",demo="453.22")
	private double list_price =0;

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_description() {
		return goods_description;
	}

	public void setGoods_description(String goods_description) {
		this.goods_description = goods_description;
	}

	public double getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(double current_price) {
		this.current_price = current_price;
	}

	public double getList_price() {
		return list_price;
	}

	public void setList_price(double list_price) {
		this.list_price = list_price;
	}
}
