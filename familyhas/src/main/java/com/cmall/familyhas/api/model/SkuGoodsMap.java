package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商品库存和购买状态  用于满减列表展示
 * @author zhouguohui
 *
 */
public class SkuGoodsMap {
	 @ZapcomApi(value="剩余促销库存")
	 private long limitStock = 0;
	 @ZapcomApi(value="购买状态")
	 private int buyStatus = 0;
	public long getLimitStock() {
		return limitStock;
	}
	public void setLimitStock(long limitStock) {
		this.limitStock = limitStock;
	}
	public int getBuyStatus() {
		return buyStatus;
	}
	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}
	 
}
