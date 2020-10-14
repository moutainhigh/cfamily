package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiForPurchaseShenPiInput extends RootInput{

	private String purchase_order_id ="";
	private String next_status ="";
	private String remark ="";
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPurchase_order_id() {
		return purchase_order_id;
	}
	public void setPurchase_order_id(String purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}
	public String getNext_status() {
		return next_status;
	}
	public void setNext_status(String next_status) {
		this.next_status = next_status;
	}
	
	


}
