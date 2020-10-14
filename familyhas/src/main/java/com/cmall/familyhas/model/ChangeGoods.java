package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 换货单信息
 * @author jlin
 *
 */
public class ChangeGoods {
	
	private String order_code;
	private String exchange_reason;
	private String transport_money="0";
	private String contacts;
	private String mobile;
	private String address;
	private String description;
	private List<ChangeGoodsDetail> GoodsDetailList=new ArrayList<ChangeGoods.ChangeGoodsDetail>();
    private String transport_people;
	

	
	public String getOrder_code() {
		return order_code;
	}



	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}


	public String getExchange_reason() {
		return exchange_reason;
	}



	public void setExchange_reason(String exchange_reason) {
		this.exchange_reason = exchange_reason;
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



	public List<ChangeGoodsDetail> getGoodsDetailList() {
		return GoodsDetailList;
	}



	public void setGoodsDetailList(List<ChangeGoodsDetail> goodsDetailList) {
		GoodsDetailList = goodsDetailList;
	}



	public static class ChangeGoodsDetail{
		private String sku_code;
		private int count;
		public String getSku_code() {
			return sku_code;
		}
		public void setSku_code(String sku_code) {
			this.sku_code = sku_code;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
	}



	public String getTransport_people() {
		return transport_people;
	}



	public void setTransport_people(String transport_people) {
		this.transport_people = transport_people;
	}
	
}
