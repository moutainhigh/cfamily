package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 推送设备信息输入参数
 * @author fq
 *
 */
public class BrowesHistoryInput extends RootInput{

	@ZapcomApi(value="商品编码",remark="要删除的商品编码，如果是多个商品编码，用英文,隔开")
	private String productCode = "";

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
}
