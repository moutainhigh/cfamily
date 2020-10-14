package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiPaymentAllInput extends RootInput{
	@ZapcomApi(value="订单编号",require=1)
	private String order_code = "";
	@ZapcomApi(value="ip地址",require=1)
	private String ip ="";
	@ZapcomApi(value="支付方式",require=1,remark="默认为支付宝支付(在线支付默认支付宝支付)",demo="在线支付：449716200001     支付宝支付：449746280003 银联支付：449746280014  连连支付：449746280013       惠家有微信APP支付：449746280005     沙皮狗微信APP支付：449746280005ShaPiGouAPP  微信小程序支付：wechat_wxss")
	private String pay_type="449716200001";
	@ZapcomApi(value="微信小程序支付时使用")
	private String openid="";
	
//	@ZapcomApi(value="openid")
//	private String openid ="";
//	@ZapcomApi(value="支付宝wap支付，跳转成功页地址")
//	private String domainName = "";
	
	
	
	
//	public String getDomainName() {
//		return domainName;
//	}
//
//	public void setDomainName(String domainName) {
//		this.domainName = domainName;
//	}
	
//	public String getOpenid() {
//		return openid;
//	}
//
//	public void setOpenid(String openid) {
//		this.openid = openid;
//	}
	
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
