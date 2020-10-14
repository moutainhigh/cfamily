package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetCouponsForProductInput extends RootInput {
	
	@ZapcomApi(value="商品编号",remark="",require=1)
	private String productCode = "";
	
	@ZapcomApi(value="来源是否积分商城",remark="1：积分商城，0：或是其他不传等默认普通商品")
	private String integralFlag = "";

	@ZapcomApi(value="是否进行分销券查询标识",remark="1：是，0：否")
	private String fxFlag = "0";
	
	public String getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getIntegralFlag() {
		return integralFlag;
	}

	public void setIntegralFlag(String integralFlag) {
		this.integralFlag = integralFlag;
	}

	
}
