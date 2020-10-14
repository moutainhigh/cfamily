package com.cmall.familyhas.api.result;

import com.cmall.ordercenter.model.MicroMessagePayment;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiCreateHmlOrderResult extends RootResultWeb {
	
	@ZapcomApi(value="订单编号",remark="订单编号")
	private String order_code = "";
	
	@ZapcomApi(value="支付宝返回的sign信息",remark="支付宝返回的sign信息")
	private String sign_detail = "";

	@ZapcomApi(value="支付链接",remark="支付链接")
	private String pay_url = "";

	@ZapcomApi(value="微信支付返回参数")
	private MicroMessagePayment micoPayment = new MicroMessagePayment();
	
	@ZapcomApi(value="是否零元支付",remark="1代表零元支付，0代表正常支付")
	private int flag = 0;
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

	public MicroMessagePayment getMicoPayment() {
		return micoPayment;
	}

	public void setMicoPayment(MicroMessagePayment micoPayment) {
		this.micoPayment = micoPayment;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}