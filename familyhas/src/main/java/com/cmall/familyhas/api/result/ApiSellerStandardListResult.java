package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 商品规格
 * @author wz
 *
 */
public class ApiSellerStandardListResult{
	@ZapcomApi(value="尺码名称")
	private String sellerStandardName;
	@ZapcomApi(value="尺码 id")
	private String SellerStandardId;
	
	public String getSellerStandardName() {
		return sellerStandardName;
	}
	public void setSellerStandardName(String sellerStandardName) {
		this.sellerStandardName = sellerStandardName;
	}
	public String getSellerStandardId() {
		return SellerStandardId;
	}
	public void setSellerStandardId(String sellerStandardId) {
		SellerStandardId = sellerStandardId;
	}
	
}
