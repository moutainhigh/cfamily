package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiCreateOrderResult extends RootResultWeb {
	
	@ZapcomApi(value="订单编号",remark="订单编号")
	private String order_code = "";
	
	@ZapcomApi(value="支付宝返回的sign信息",remark="支付宝返回的sign信息")
	private String sign_detail = "";

	@ZapcomApi(value="支付宝支付链接",remark="支付宝支付链接")
	private String pay_url = "";
	
	@ZapcomApi(value="银联支付要使用的fn号码",remark="201603031512272347688")
	private String fnCode = "";

	@ZapcomApi(value="微信支付相关参数",remark="微信支付相关参数")
	WeChatpaymentResult chatpayment = new WeChatpaymentResult();
	
	@ZapcomApi(value="银联支付返回参数")
	private UnionPaymentResult unionPayResult = new UnionPaymentResult();
	
	@ZapcomApi(value="无库存商品列表")
	private List<NoStockOrFailureGoods> noStockOrFailureGoods = new ArrayList<NoStockOrFailureGoods>();
	
	@ZapcomApi(value = "专门针对加价购活动失效的提示字段")
	private String jjgTips = "";
	
	public String getJjgTips() {
		return jjgTips;
	}

	public void setJjgTips(String jjgTips) {
		this.jjgTips = jjgTips;
	}
	
	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getSign_detail() {
		return sign_detail;
	}

	public void setSign_detail(String sign_detail) {
		this.sign_detail = sign_detail;
	}

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}

	public WeChatpaymentResult getChatpayment() {
		return chatpayment;
	}

	public void setChatpayment(WeChatpaymentResult chatpayment) {
		this.chatpayment = chatpayment;
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

	public List<NoStockOrFailureGoods> getNoStockOrFailureGoods() {
		return noStockOrFailureGoods;
	}

	public void setNoStockOrFailureGoods(List<NoStockOrFailureGoods> noStockOrFailureGoods) {
		this.noStockOrFailureGoods = noStockOrFailureGoods;
	}
	
}