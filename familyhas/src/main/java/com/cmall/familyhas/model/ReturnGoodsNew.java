package com.cmall.familyhas.model;

import com.srnpr.zapcom.basemodel.MObjMap;

public class ReturnGoodsNew {

	private String order_code;
	private String description;
	private String transport_people;
	private String return_reason_code;
	private String goods_receipt;
	private String freight;
	private String contacts;
	private String mobile;
	private String address;
	private String receiver_area_code;
	private String flag_return_goods;
	
	private MObjMap<String, Integer> detailMap = new MObjMap<String, Integer>();// 退货详情

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

	public String getTransport_people() {
		return transport_people;
	}

	public void setTransport_people(String transport_people) {
		this.transport_people = transport_people;
	}

	public String getReturn_reason_code() {
		return return_reason_code;
	}

	public void setReturn_reason_code(String return_reason_code) {
		this.return_reason_code = return_reason_code;
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

	public MObjMap<String, Integer> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(MObjMap<String, Integer> detailMap) {
		this.detailMap = detailMap;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReceiver_area_code() {
		return receiver_area_code;
	}

	public void setReceiver_area_code(String receiver_area_code) {
		this.receiver_area_code = receiver_area_code;
	}

	public String getFlag_return_goods() {
		return flag_return_goods;
	}

	public void setFlag_return_goods(String flag_return_goods) {
		this.flag_return_goods = flag_return_goods;
	}

}
