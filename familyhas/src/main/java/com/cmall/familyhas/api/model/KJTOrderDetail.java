package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 跨境通订单详情
 * @author pangjh
 *
 */
public class KJTOrderDetail {
	
	@ZapcomApi(value="商品名称")
	private String sku_name;

	/**
	 * 获取商品名称
	 * @return
	 */
	public String getSku_name() {
		return sku_name;
	}

	/**
	 * 设置商品名称
	 * @param sku_name
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}
	
	

}
