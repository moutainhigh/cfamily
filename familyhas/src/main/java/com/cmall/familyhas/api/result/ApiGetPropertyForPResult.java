package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Propertyinfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetPropertyForPResult extends RootResult {
    
	@ZapcomApi(value = "商品编码")
    private String productCode  = ""  ;
	
	@ZapcomApi(value = "商品属性")
    private List<Propertyinfo> propertyinfo  =  new ArrayList<Propertyinfo>();

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<Propertyinfo> getPropertyinfo() {
		return propertyinfo;
	}

	public void setPropertyinfo(List<Propertyinfo> propertyinfo) {
		this.propertyinfo = propertyinfo;
	}
	
}