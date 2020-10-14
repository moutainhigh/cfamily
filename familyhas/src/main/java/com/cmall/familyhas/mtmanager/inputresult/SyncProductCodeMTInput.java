package com.cmall.familyhas.mtmanager.inputresult;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * mt管家产品同步信息输入参数
 * @author pang_jhui
 *
 */
public class SyncProductCodeMTInput extends RootInput {
	

	
	@ZapcomApi(value="商品编码",require = 1)
	private String productCode = "";

	/**
	 * 获取产品代码
	 * @return
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * 设置产品代码
	 * @param productCode
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
	
	
	

}
