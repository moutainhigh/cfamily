package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiPaymentTypeAllResult extends RootResultWeb{
	@ZapcomApi(value="支付类型",remark="在线支付：449716200001      支付宝支付:449746280003,  微信支付:449746280005, applePay支付:449746280013, 银联支付:449746280014")
	private List<String> paymentTypeAll = new ArrayList<String>();
	
	@ZapcomApi(value="订单商品明细")
	private List<ProductItem> productList = new ArrayList<ApiPaymentTypeAllResult.ProductItem>();
	@ZapcomApi(value="应付款")
	private BigDecimal dueMoney = BigDecimal.ZERO; 

	public List<String> getPaymentTypeAll() {
		return paymentTypeAll;
	}

	public void setPaymentTypeAll(List<String> paymentTypeAll) {
		this.paymentTypeAll = paymentTypeAll;
	}
	
	public List<ProductItem> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductItem> productList) {
		this.productList = productList;
	}

	public BigDecimal getDueMoney() {
		return dueMoney;
	}

	public void setDueMoney(BigDecimal dueMoney) {
		this.dueMoney = dueMoney;
	}




	public static class ProductItem {
		
		@ZapcomApi(value="商品名称")
		private String productName;

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}
	}
}
