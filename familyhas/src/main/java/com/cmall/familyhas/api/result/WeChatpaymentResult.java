package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class WeChatpaymentResult {
	@ZapcomApi(value="商家在微信开放平台申请的应用id")
	private String appid = "";
	@ZapcomApi(value="商户号")
	private String mch_id = "";
	@ZapcomApi(value="微信返回的随机字符串")
	private String nonce_str = "";
	@ZapcomApi(value="微信返回的签名")
	private String sign = "";
	@ZapcomApi(value="预支付交易会话标识",remark="微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时")
	private String prepay_id = "";
	@ZapcomApi(value="交易类型",remark="调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP")
	private String trade_type = "";
	@ZapcomApi(remark="业务结果",value="SUCCESS/FAIL")
	private String result_code = "";
	@ZapcomApi(remark="返回状态码（此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断）",value="SUCCESS/FAIL")
	private String return_code = "";
	@ZapcomApi(remark="返回信息",value="SUCCESS/FAIL")
	private String return_msg = "";
	@ZapcomApi(value="时间戳")
	private String timestamp = "";
	
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
	
	
}
