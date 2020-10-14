package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiWechatNetParamsInput extends RootInput{
	@ZapcomApi(value = ".net自己的商户号")
	private String merchantid="";
	@ZapcomApi(value = "交易类型",demo="oauth")
	private String tradetype="";
	@ZapcomApi(value = "授权类型",demo="b为基本(仅返回openid等简单信息),f为高级(返回可以取得的所有信息).")
	private String orderno="";
	@ZapcomApi(value = "交易日期",demo="yyyyMMddHHmmss")
	private String tradetime="";
	@ZapcomApi(value = "授权登陆的类型",demo="WeiXin代表微信授权登陆,QQ代表QQ授权登陆,AliPay代表支付宝授权登陆,WeiBo代表微博授权登陆")
	private String tradeCode="";
	@ZapcomApi(value = "使用哪个账号授权,测试环境请传5,正式环境再定")
	private String channelid="";
	@ZapcomApi(value = "回转地址,授权成功后返回的地址(微信授权登陆时不能超过80位)")
	private String callBackURL="";
	public String getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	public String getTradetype() {
		return tradetype;
	}
	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getTradetime() {
		return tradetime;
	}
	public void setTradetime(String tradetime) {
		this.tradetime = tradetime;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getCallBackURL() {
		return callBackURL;
	}
	public void setCallBackURL(String callBackURL) {
		this.callBackURL = callBackURL;
	}
}