package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiDeleteSkuToShopCartInput extends RootInput {

	@ZapcomApi(value = "买家编号", remark = "可为空，默认取当前登录人的编号", demo = "123456")
	private String buyer_code = "";
	
	@ZapcomApi(value = "商品列表" ,require = 1)
	private List<String> skuCodes = new ArrayList<String>();

	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

	public List<String> getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(List<String> skuCodes) {
		this.skuCodes = skuCodes;
	}
	
}
