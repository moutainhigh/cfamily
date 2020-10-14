package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiCollectionProductInput extends RootInput {
	
	@ZapcomApi(value = "操作类型", remark = "0：取消收藏，1:收藏商品", demo = "0",require=1)
	private String operateType = "";

	@ZapcomApi(value = "商品编码",require=1)
	private List<String> productCode = new ArrayList<String>();
	
	@ZapcomApi(value = "sku", remark = "534版本开始，收藏时传入sku码，取消收藏不用传")
	private String skuCode = "";
	
	@ZapcomApi(value = "sku销售价", remark = "534版本开始，收藏时传入sku销售价，取消收藏不用传")
	private String skuPrice = "";


	
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public List<String> getProductCode() {
		return productCode;
	}

	public void setProductCode(List<String> productCode) {
		this.productCode = productCode;
	}

}
