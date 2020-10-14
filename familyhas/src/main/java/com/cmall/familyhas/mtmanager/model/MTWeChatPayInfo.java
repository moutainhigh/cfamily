package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 微信支付信息
 * @author pang_jhui
 *
 */
public class MTWeChatPayInfo {
	
	@ZapcomApi(value="微信公众号ID")
	private String appid;
	
	@ZapcomApi(value="商户编号")
	private String mch_id;
	
	@ZapcomApi(value="订单编号")
	private String out_trade_no;
	
	@ZapcomApi(value="微信支付订单号")
	private String transaction_id;
	
	@ZapcomApi(value="微信支付完成时间")
	private String time_end;
	
	@ZapcomApi(value="签名")
	private String sign;
	
	@ZapcomApi(value="交易类型")
	private String trade_type;
	
	@ZapcomApi(value="付款银行")
	private String bank_type;
	
	@ZapcomApi(value="支付总金额")
	private String total_fee;
	
	@ZapcomApi(value="微信接口处理结果")
	private String result_code;
	
	@ZapcomApi(value="微信接口请求参数")
	private String param_value;
	
	@ZapcomApi(value="微信支付是否成功 1：成功 0未成功")
	private int flag_success;
	
	@ZapcomApi(value="支付状态")
	private String mark;

	/**
	 * 获取微信公众号
	 * @return
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * 设置微信公众号
	 * @param appid
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * 获取商户编号
	 * @return
	 */
	public String getMch_id() {
		return mch_id;
	}

	/**
	 * 设置商户编号
	 * @param mch_id
	 */
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}

	/**
	 * 设置订单编号
	 * @param out_trade_no
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	/**
	 * 获取微信支付单号
	 * @return
	 */
	public String getTransaction_id() {
		return transaction_id;
	}

	/**
	 * 设置微信支付单号
	 * @param transaction_id
	 */
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	/**
	 * 获取微信支付完成时间
	 * @return
	 */
	public String getTime_end() {
		return time_end;
	}

	/**
	 * 设置微信支付完成时间
	 * @param time_end
	 */
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	/**
	 * 获取接口签名
	 * @return
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 设置接口签名
	 * @param sign
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * 获取交易类型
	 * @return
	 */
	public String getTrade_type() {
		return trade_type;
	}

	/**
	 * 设置交易类型
	 * @param trade_type
	 */
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	/**
	 * 获取付款银行
	 * @return
	 */
	public String getBank_type() {
		return bank_type;
	}

	/**
	 * 设置付款银行
	 * @param bank_type
	 */
	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	/**
	 * 获取支付总费用
	 * @return
	 */
	public String getTotal_fee() {
		return total_fee;
	}

	/**
	 * 设置支付总金额
	 * @param total_fee
	 */
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	/**
	 * 获取响应结果
	 * @return
	 */
	public String getResult_code() {
		return result_code;
	}

	/**
	 * 设置响应结果
	 * @param result_code
	 */
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	/**
	 * 获取请求参数
	 * @return
	 */
	public String getParam_value() {
		return param_value;
	}

	/**
	 * 设置请求参数
	 * @param param_value
	 */
	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	/**
	 * 获取接口调用是否成功
	 * @return
	 */
	public int getFlag_success() {
		return flag_success;
	}

	/**
	 * 设置接口调用是否成功
	 * @param flag_success
	 */
	public void setFlag_success(int flag_success) {
		this.flag_success = flag_success;
	}

	/**
	 * 获取支付状态
	 * @return
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * 设置支付状态
	 * @param mark
	 */
	public void setMark(String mark) {
		this.mark = mark;
	} 
	

}
