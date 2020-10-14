package com.cmall.familyhas.api.result;

import com.srnpr.xmaspay.response.ApplePayResponse;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiPaymentAllResult extends RootResultWeb{
	@ZapcomApi(value="支付宝支付返回参数")
	private AlipayPaymentResult alipayPaymentResult = new AlipayPaymentResult();
	@ZapcomApi(value="微信支付返回参数")
	private WeChatpaymentResult weChatpaymentResult = new WeChatpaymentResult();
	@ZapcomApi(value="练练支付返回参数")
	private ApplePayResponse applePayResult = new ApplePayResponse();
	@ZapcomApi(value="银联支付返回参数")
	private UnionPaymentResult unionPayResult = new UnionPaymentResult();
	@ZapcomApi(value="银联支付流水号")
	private String fnCode ="";
	@ZapcomApi(value="微信小程序支付参数")
	private String jsapiparam;
	
	public AlipayPaymentResult getAlipayPaymentResult() {
		return alipayPaymentResult;
	}
	public void setAlipayPaymentResult(AlipayPaymentResult alipayPaymentResult) {
		this.alipayPaymentResult = alipayPaymentResult;
	}
	public WeChatpaymentResult getWeChatpaymentResult() {
		return weChatpaymentResult;
	}
	public void setWeChatpaymentResult(WeChatpaymentResult weChatpaymentResult) {
		this.weChatpaymentResult = weChatpaymentResult;
	}
	public ApplePayResponse getApplePayResult() {
		return applePayResult;
	}
	public void setApplePayResult(ApplePayResponse applePayResult) {
		this.applePayResult = applePayResult;
	}
	public String getFnCode() {
		return fnCode;
	}
	public void setFnCode(String fnCode) {
		this.fnCode = fnCode;
	}
	public UnionPaymentResult getUnionPayResult() {
		return unionPayResult;
	}
	public void setUnionPayResult(UnionPaymentResult unionPayResult) {
		this.unionPayResult = unionPayResult;
	}
	public String getJsapiparam() {
		return jsapiparam;
	}
	public void setJsapiparam(String jsapiparam) {
		this.jsapiparam = jsapiparam;
	}

}
