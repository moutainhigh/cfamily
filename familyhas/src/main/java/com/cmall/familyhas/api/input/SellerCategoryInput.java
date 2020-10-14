package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class SellerCategoryInput extends RootInput{
	@ZapcomApi(value="平台编码",remark="平台编码",demo="SI2003")
	private String sellerCode = "";
	@ZapcomApi(value="父节点编码",remark="父节点编码",demo="44971604")  
	private String parentCode = "";
	
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
