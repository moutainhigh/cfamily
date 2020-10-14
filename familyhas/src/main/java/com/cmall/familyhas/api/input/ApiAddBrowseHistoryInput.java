package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiAddBrowseHistoryInput extends RootInput{
	@ZapcomApi(value="商品编码",remark="商品编码",require=1)
	private List<String> productCodes = new ArrayList<String>();
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	public List<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	
}
