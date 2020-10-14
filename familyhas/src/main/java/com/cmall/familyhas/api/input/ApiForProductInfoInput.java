package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForProductInfoInput  extends RootInput{
	@ZapcomApi(value = "商品编号")
	private String productCode="";
	private String subsidy = "";

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	/**
	 * @return the productCode
	 */
	public String getSubsidy() {
		return subsidy;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
	}

	
}
