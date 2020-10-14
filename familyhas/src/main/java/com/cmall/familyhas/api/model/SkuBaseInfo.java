package com.cmall.familyhas.api.model;


public class SkuBaseInfo {
	private String skuCode = "";
	private String skuName = "";
	private String subsidy = "";
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSubsidy() {
		return subsidy;
	}
	public void setSubsidy(String is_subsidy) {
		this.subsidy = is_subsidy;
	}
	
}