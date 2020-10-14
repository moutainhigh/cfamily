package com.cmall.familyhas.mtmanager.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * MT管家订单支付信息
 * @author pang_jhui
 *
 */
public class MTOrderPayInfo {
	
	@ZapcomApi(value="订单编号",remark = "惠家有生成的订单编号",require = 1)
	private String order_code = "";
	
	@ZapcomApi(value="支付类型",remark = "449746280003:支付宝支付,449746280005:微信支付",require = 1)
	private String pay_type = "";
	
	@ZapcomApi(value="支付备注")
	private String pay_remark = "";
	
	@ZapcomApi(value="订单金额")
	private BigDecimal payed_all_fee = BigDecimal.ZERO;
	
	/**
	 * 获取订单编号
	 * @return
	 */
	public String getOrder_code() {
		return order_code;
	}

	/**
	 * 设置订单编号
	 * @param order_code
	 */
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	/**
	 * 获取支付类型
	 * @return
	 */
	public String getPay_type() {
		return pay_type;
	}

	/**
	 * 设置支付类型
	 * @param pay_type
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	/**
	 * 支付备注
	 * @return
	 */
	public String getPay_remark() {
		return pay_remark;
	}

	/**
	 * 设置支付备注
	 * @param pay_remark
	 */
	public void setPay_remark(String pay_remark) {
		this.pay_remark = pay_remark;
	}

	/**
	 * 支付总费用
	 * @return
	 */
	public BigDecimal getPayed_all_fee() {
		return payed_all_fee;
	}

	/**
	 * 设置支付总费用
	 * @param payed_all_fee
	 */
	public void setPayed_all_fee(BigDecimal payed_all_fee) {
		this.payed_all_fee = payed_all_fee;
	}


}
