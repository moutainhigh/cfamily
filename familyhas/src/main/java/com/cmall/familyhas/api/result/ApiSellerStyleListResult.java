package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 商品款式
 * @author wz
 *
 */
public class ApiSellerStyleListResult{
	@ZapcomApi(value="款式名称")
	private String sellerStyleName;
	@ZapcomApi(value="款式 id")
	private String sellerStyleId;
	
	public String getSellerStyleName() {
		return sellerStyleName;
	}
	public void setSellerStyleName(String sellerStyleName) {
		this.sellerStyleName = sellerStyleName;
	}
	public String getSellerStyleId() {
		return sellerStyleId;
	}
	public void setSellerStyleId(String sellerStyleId) {
		this.sellerStyleId = sellerStyleId;
	}
	
	
}
