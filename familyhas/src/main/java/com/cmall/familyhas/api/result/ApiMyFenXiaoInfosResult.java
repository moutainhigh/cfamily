package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiMyFenXiaoInfosResult extends RootResultWeb {
	@ZapcomApi(value = "可提现金额")
	private ApiRealMoney realMoney = new ApiRealMoney();
	@ZapcomApi(value = "预估收益")
	ApiPredictMoney preMoney = new ApiPredictMoney();
	@ZapcomApi(value = "分销订单")
	ApiFXOrders fxOrders = new ApiFXOrders();
	@ZapcomApi(value = "我的粉丝")
	ApiMyFans myFans = new ApiMyFans();
	public ApiRealMoney getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(ApiRealMoney realMoney) {
		this.realMoney = realMoney;
	}
	public ApiPredictMoney getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(ApiPredictMoney preMoney) {
		this.preMoney = preMoney;
	}
	public ApiFXOrders getFxOrders() {
		return fxOrders;
	}
	public void setFxOrders(ApiFXOrders fxOrders) {
		this.fxOrders = fxOrders;
	}
	public ApiMyFans getMyFans() {
		return myFans;
	}
	public void setMyFans(ApiMyFans myFans) {
		this.myFans = myFans;
	}
	
	
}
