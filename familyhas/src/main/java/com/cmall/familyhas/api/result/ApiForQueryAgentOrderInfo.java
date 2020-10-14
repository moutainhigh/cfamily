package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 分销商品订单信息
 */
public class ApiForQueryAgentOrderInfo{
	
	@ZapcomApi(value="订单状态",remark="下单成功-未付款：4497153900010001，下单成功-未发货：4497153900010002，已发货：4497153900010003，已收货：4497153900010004，交易成功：4497153900010005，交易失败：4497153900010006，等待审核：4497153900010008")
	private String order_status = "";
	@ZapcomApi(value="订单号",remark="")
	private String order_code = "";
	@ZapcomApi(value="订单生成时间",remark="")
	private String create_time = "";
	@ZapcomApi(value="实付总价",remark="")
	private String due_money = "0";
	@ZapcomApi(value="预估收益",remark="")
	private String predict_money = "";
	@ZapcomApi(value="商品总件数",remark="")
	private String total_product_number = "0";
	@ZapcomApi(value="每个订单的商品信息",remark="")
	private List<ApiSellerListResult> apiSellerList = new ArrayList<ApiSellerListResult>();
	
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPredict_money() {
		return predict_money;
	}
	public void setPredict_money(String predict_money) {
		this.predict_money = predict_money;
	}
	public List<ApiSellerListResult> getApiSellerList() {
		return apiSellerList;
	}
	public void setApiSellerList(List<ApiSellerListResult> apiSellerList) {
		this.apiSellerList = apiSellerList;
	}
	public String getDue_money() {
		return due_money;
	}
	public void setDue_money(String due_money) {
		this.due_money = due_money;
	}
	public String getTotal_product_number() {
		return total_product_number;
	}
	public void setTotal_product_number(String total_product_number) {
		this.total_product_number = total_product_number;
	}
	
}
