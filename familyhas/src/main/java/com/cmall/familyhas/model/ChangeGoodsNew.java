package com.cmall.familyhas.model;

import com.srnpr.zapcom.basemodel.MObjMap;

public class ChangeGoodsNew {

	private String order_code;
	private String description;
	private String mobile;
	private String transport_people;
	private String after_sale_person;
	private String after_sale_mobile;
	private String after_sale_address;
	private String after_sale_postcode;
	private String asale_reason;
	private String goods_receipt;
	private String freight;
	private String flag_return_goods;
	
	private MObjMap<String, Integer> detailMap = new MObjMap<String, Integer>();// 换货详情

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTransport_people() {
		return transport_people;
	}

	public void setTransport_people(String transport_people) {
		this.transport_people = transport_people;
	}

	public String getAfter_sale_person() {
		return after_sale_person;
	}

	public void setAfter_sale_person(String after_sale_person) {
		this.after_sale_person = after_sale_person;
	}

	public String getAfter_sale_mobile() {
		return after_sale_mobile;
	}

	public void setAfter_sale_mobile(String after_sale_mobile) {
		this.after_sale_mobile = after_sale_mobile;
	}

	public String getAfter_sale_address() {
		return after_sale_address;
	}

	public void setAfter_sale_address(String after_sale_address) {
		this.after_sale_address = after_sale_address;
	}

	public String getAfter_sale_postcode() {
		return after_sale_postcode;
	}

	public void setAfter_sale_postcode(String after_sale_postcode) {
		this.after_sale_postcode = after_sale_postcode;
	}

	public String getAsale_reason() {
		return asale_reason;
	}

	public void setAsale_reason(String asale_reason) {
		this.asale_reason = asale_reason;
	}

	public String getGoods_receipt() {
		return goods_receipt;
	}

	public void setGoods_receipt(String goods_receipt) {
		this.goods_receipt = goods_receipt;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getFlag_return_goods() {
		return flag_return_goods;
	}

	public void setFlag_return_goods(String flag_return_goods) {
		this.flag_return_goods = flag_return_goods;
	}

	public MObjMap<String, Integer> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(MObjMap<String, Integer> detailMap) {
		this.detailMap = detailMap;
	}

	
}
