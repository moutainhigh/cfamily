package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 支付宝支付信息
 * @author pang_jhui
 *
 */
public class MTAliPayInfo {
	
	@ZapcomApi(value="订单编号")
	private String out_trade_no;
	
	@ZapcomApi(value="交易状态")
	private String trade_status;
	
	@ZapcomApi(value="签名方式")
	private String sign_type;
	
	@ZapcomApi(value="签名")
	private String sign;
	
	@ZapcomApi(value="支付宝交易号")
	private String trade_no;
	
	@ZapcomApi(value="买家支付宝账号")
	private String buyer_email;
	
	@ZapcomApi(value="交易金额")
	private String total_fee;
	
	@ZapcomApi(value="卖家支付宝帐号")
	private String seller_email;
	
	@ZapcomApi(value="交易付款时间")
	private String gmt_payment;
	
	@ZapcomApi(value="交易创建时间")
	private String gmt_create;
	
	@ZapcomApi(value="请求参数值")
	private String param_value;
	
	@ZapcomApi(value="请求是否成功",remark=" 1：成功 0：失败")
	private String flag_success;
	
	@ZapcomApi(value="支付状态")
	private String mark;

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
	 * 获取交易状态
	 * @return
	 */
	public String getTrade_status() {
		return trade_status;
	}

	/**
	 * 设置交易状态
	 * @param trade_status
	 */
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	/**
	 * 获取签名类型
	 * @return
	 */
	public String getSign_type() {
		return sign_type;
	}

	/**
	 * 设置签名类型
	 * @param sign_type
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	/**
	 * 获取签名
	 * @return
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 设置签名
	 * @param sign
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * 获取支付宝交易号
	 * @return
	 */
	public String getTrade_no() {
		return trade_no;
	}

	/**
	 * 设置支付宝交易号
	 * @param trade_no
	 */
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	/**
	 * 获取买家支付宝账号
	 * @return
	 */
	public String getBuyer_email() {
		return buyer_email;
	}

	/**
	 * 设置买家支付包帐号
	 * @param buyer_email
	 */
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	/**
	 * 获取总交易金额
	 * @return
	 */
	public String getTotal_fee() {
		return total_fee;
	}

	/**
	 * 设置总交易金额
	 * @param total_fee
	 */
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	/**
	 * 获取卖家支付宝账号
	 * @return
	 */
	public String getSeller_email() {
		return seller_email;
	}

	/**
	 * 设置卖家支付宝帐号
	 * @param seller_email
	 */
	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	/**
	 * 获取交易付款时间
	 * @return
	 */
	public String getGmt_payment() {
		return gmt_payment;
	}

	/**
	 * 设置交易付款时间
	 * @param gmt_payment
	 */
	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	/**
	 * 获取交易创建时间
	 * @return
	 */
	public String getGmt_create() {
		return gmt_create;
	}

	/**
	 * 设置交易创建时间
	 * @param gmt_create
	 */
	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	/**
	 * 获取所有参数值
	 * @return
	 */
	public String getParam_value() {
		return param_value;
	}

	/**
	 * 设置参数值
	 * @param param_value
	 */
	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	/**
	 * 获取交易是否成功
	 * @return
	 */
	public String getFlag_success() {
		return flag_success;
	}

	/**
	 * 设置交易是否成功
	 * @param flag_success
	 */
	public void setFlag_success(String flag_success) {
		this.flag_success = flag_success;
	}

	/**
	 * 获取交易状态
	 * @return
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * 设置交易状态
	 * @param mark
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}


}
