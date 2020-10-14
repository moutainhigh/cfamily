package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	采购基本信息
*   zhangbo
*/
public class PurchaseBaseInfo  {
	
	@ZapcomApi(value = "采购单编号")
	private String purchase_order_id = "";
	
	@ZapcomApi(value = "商品数量")
	private String purchase_num = "";
	
	@ZapcomApi(value = "采购总价")
	private String purchase_money = "";
	
	@ZapcomApi(value = "下单手机号")
	private String phone = "";
	
	@ZapcomApi(value = "采购说明")
	private String purchase_text = "";
	
	@ZapcomApi(value = "订单商品",remark="skuCode_costPrice_sellPrice_num_ifselected")
	private String basic_order_skus = "";
	
	@ZapcomApi(value = "收货地址编号")
	private String adress_id = "";
	
	public String getPurchase_order_id() {
		return purchase_order_id;
	}

	public void setPurchase_order_id(String purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}

	public String getPurchase_num() {
		return purchase_num;
	}

	public void setPurchase_num(String purchase_num) {
		this.purchase_num = purchase_num;
	}

	public String getPurchase_money() {
		return purchase_money;
	}

	public void setPurchase_money(String purchase_money) {
		this.purchase_money = purchase_money;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPurchase_text() {
		return purchase_text;
	}

	public void setPurchase_text(String purchase_text) {
		this.purchase_text = purchase_text;
	}

	public String getBasic_order_skus() {
		return basic_order_skus;
	}

	public void setBasic_order_skus(String basic_order_skus) {
		this.basic_order_skus = basic_order_skus;
	}

	public String getAdress_id() {
		return adress_id;
	}

	public void setAdress_id(String adress_id) {
		this.adress_id = adress_id;
	}


}

