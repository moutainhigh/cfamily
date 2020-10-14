package com.cmall.familyhas.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 转换输入类
 * @author shiyz
 *
 */
public class PunishModel extends BaseClass {

	private String  orderCode  = "";
	
	private  String orderTime = "";
	
	private String productCode = "";

	private String productName = "";
	
	private BigDecimal productCost ;
	
	private BigDecimal productSellPrice;
	
	private BigDecimal punishMoney;
	
	private String punishReason;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductCost() {
		return productCost;
	}

	public void setProductCost(BigDecimal productCost) {
		this.productCost = productCost;
	}

	public BigDecimal getProductSellPrice() {
		return productSellPrice;
	}

	public void setProductSellPrice(BigDecimal productSellPrice) {
		this.productSellPrice = productSellPrice;
	}

	public BigDecimal getPunishMoney() {
		return punishMoney;
	}

	public void setPunishMoney(BigDecimal punishMoney) {
		this.punishMoney = punishMoney;
	}

	public String getPunishReason() {
		return punishReason;
	}

	public void setPunishReason(String punishReason) {
		this.punishReason = punishReason;
	}
	
	
}




