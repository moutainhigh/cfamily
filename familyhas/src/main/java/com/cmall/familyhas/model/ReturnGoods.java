package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;

public class ReturnGoods {
	private String order_code;
	private String return_reason;
	private String transport_money;
	private String contacts;
	private String mobile;
	private String address;
	private String description;
	private String transport_people;
//	private List<ReturnGoodsDetail> GoodsDetailList = new ArrayList<ReturnGoods.ReturnGoodsDetail>();

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getReturn_reason() {
		return return_reason;
	}

	public void setReturn_reason(String return_reason) {
		this.return_reason = return_reason;
	}

	public String getTransport_money() {
		return transport_money;
	}

	public void setTransport_money(String transport_money) {
		this.transport_money = transport_money;
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

//	public List<ReturnGoodsDetail> getGoodsDetailList() {
//		return GoodsDetailList;
//	}
//
//	public void setGoodsDetailList(List<ReturnGoodsDetail> goodsDetailList) {
//		GoodsDetailList = goodsDetailList;
//	}
//
//	public static class ReturnGoodsDetail {
//		private String sku_code;
//		private int count;
//
//		public String getSku_code() {
//			return sku_code;
//		}
//
//		public void setSku_code(String sku_code) {
//			this.sku_code = sku_code;
//		}
//
//		public int getCount() {
//			return count;
//		}
//
//		public void setCount(int count) {
//			this.count = count;
//		}
//
//	}
	
}
