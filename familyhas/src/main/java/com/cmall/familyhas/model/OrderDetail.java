package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	采购信息
*   zhangbo
*/
public class OrderDetail  {
	@ZapcomApi(value = "采购单编号")
	private String purchase_order_id = "";
	private String sku_code = "";
	private String product_code = "";
	private String product_name = "";
	private String cost_money = "0";
	private String sell_money = "0";
	private String sku_num = "1";
	private String if_selected="";
	private String rowSumMoney = "";
	private String product_img = "";
	private String product_property="";
	private String order_id="";
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getProduct_img() {
		return product_img;
	}
	public void setProduct_img(String product_img) {
		this.product_img = product_img;
	}
	public String getProduct_property() {
		return product_property;
	}
	public void setProduct_property(String product_property) {
		this.product_property = product_property;
	}
	public String getRowSumMoney() {
		return rowSumMoney;
	}
	public void setRowSumMoney(String rowSumMoney) {
		this.rowSumMoney = rowSumMoney;
	}
	public String getSell_money() {
		return sell_money;
	}
	public void setSell_money(String sell_money) {
		this.sell_money = sell_money;
	}
	public String getIf_selected() {
		return if_selected;
	}
	public void setIf_selected(String if_selected) {
		this.if_selected = if_selected;
	}
	public String getSku_num() {
		return sku_num;
	}
	public void setSku_num(String sku_num) {
		this.sku_num = sku_num;
	}
	public String getPurchase_order_id() {
		return purchase_order_id;
	}
	public void setPurchase_order_id(String purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
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
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getCost_money() {
		return cost_money;
	}
	public void setCost_money(String cost_money) {
		this.cost_money = cost_money;
	}



	
	

}

