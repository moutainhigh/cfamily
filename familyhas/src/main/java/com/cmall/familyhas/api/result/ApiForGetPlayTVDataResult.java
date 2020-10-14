package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetPlayTVDataResult extends RootResultWeb {

	@ZapcomApi(value = "今日正在直播商品编码")
	private String productCode = "";

	@ZapcomApi(value = "正在直播商品数量")
	private int num = 0;

	/**
	 * 获取  productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * 设置 
	 * @param productCode 
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
