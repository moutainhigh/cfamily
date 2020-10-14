package com.cmall.familyhas.api.result;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class FXDetail extends RootResultWeb {

	@ZapcomApi(value="退款状态",remark="0：待退款 1：已退款  2:提现失败")
	private String state = "";
	@ZapcomApi(value="申请时间")
	private String createime = "";
	@ZapcomApi(value="退款金额")
	private String withdrawMoney = "";
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreateime() {
		return createime;
	}
	public void setCreateime(String createime) {
		this.createime = createime;
	}
	public String getWithdrawMoney() {
		return withdrawMoney;
	}
	public void setWithdrawMoney(String withdrawMoney) {
		this.withdrawMoney = withdrawMoney;
	}	
	
	

}
