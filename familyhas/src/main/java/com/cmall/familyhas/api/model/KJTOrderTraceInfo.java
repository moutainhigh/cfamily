package com.cmall.familyhas.api.model;

import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 跨境通运单信息
 * @author pangjh
 *
 */
public class KJTOrderTraceInfo {
	
	@ZapcomApi(value="运单编号")
	private String waybill;
	
	@ZapcomApi(value="拆单后的订单序列号")
	private String order_code_seq;
	
	@ZapcomApi(value="物流公司名称")
	private String logisticse_name;
	
	@ZapcomApi(value="订单编号")
	private String order_code;
	
	@ZapcomApi(value="订单详情")
	private List<KJTOrderDetail> productList;
	
	@ZapcomApi(value="快递信息")
	private List<KJTExpressDetail> expressList;

	/**
	 * 获取运单编号
	 * @return
	 */
	public String getWaybill() {
		return waybill;
	}

	/**
	 * 设置运单编号
	 * @param waybill
	 */
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}

	/**
	 * 获取拆单后的订单序列号
	 * @return
	 */
	public String getOrder_code_seq() {
		return order_code_seq;
	}

	/**
	 * 设置拆单后的订单序列号
	 * @param order_code_seq
	 */
	public void setOrder_code_seq(String order_code_seq) {
		this.order_code_seq = order_code_seq;
	}

	/**
	 * 获取物流公司名称
	 * @return
	 */
	public String getLogisticse_name() {
		return logisticse_name;
	}

	/**
	 * 设置物流公司名称
	 * @param logisticseName
	 */
	public void setLogisticse_name(String logisticse_name) {
		this.logisticse_name = logisticse_name;
	}

	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOrder_code() {
		return order_code;
	}

	/**
	 * 设置订单编号
	 * @param order_code
	 */
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	/**
	 * 获取定的那详情信息集合
	 * @return
	 */
	public List<KJTOrderDetail> getProductList() {
		return productList;
	}

	/**
	 * 设置订单详情集合
	 * @param productList
	 */
	public void setProductList(List<KJTOrderDetail> productList) {
		this.productList = productList;
	}

	/**
	 * 获取快递信息
	 * @return
	 */
	public List<KJTExpressDetail> getExpressList() {
		return expressList;
	}

	/**
	 * 设置快递信息
	 * @param expressList
	 */
	public void setExpressList(List<KJTExpressDetail> expressList) {
		this.expressList = expressList;
	}


}
