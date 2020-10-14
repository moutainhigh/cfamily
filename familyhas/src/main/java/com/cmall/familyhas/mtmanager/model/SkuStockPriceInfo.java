package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * sku库存及价格接口
 * @author xiegj
 *
 */
public class SkuStockPriceInfo extends RootInput {

	@ZapcomApi(value = "sku编号", remark = "", demo = "8019123456")
	private String skuCode = "";
	@ZapcomApi(value = "商品价格", remark = "", demo = "88.00")
	private Double price = 0.00;
	@ZapcomApi(value = "商品库存", remark = "", demo = "88")
	private long stock = 0;
	@ZapcomApi(value = "是否可卖", remark = "1：是，0：否", demo = "1")
	private String flag = "1";
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public long getStock() {
		return stock;
	}
	public void setStock(long stock) {
		this.stock = stock;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
