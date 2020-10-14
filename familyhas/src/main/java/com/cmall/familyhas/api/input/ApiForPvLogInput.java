package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @Author fufu
* @Time 2020-8-18 11:58:39 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPvLogInput extends RootInput {
	@ZapcomApi(value="类型",remark="449748740007	赠送雨滴页下单\r\n" + 
			"449748740006	逛会场下单\r\n" + 
			"449748740005	看商品下单\r\n" + 
			"449748740004	为您推荐\r\n" + 
			"449748740003	节目表\r\n" + 
			"449748740002	福利大转盘\r\n" + 
			"449748740001	积分打卡\r\n" + 
			"449748740008   LD短信支付成功节目表\r\n"
			+ "449748740009   LD短信支付成功猜你喜欢",demo="",require=1)
	private String sourcePageType = "";
	
	@ZapcomApi(value="手机号，非惠家有登陆功能，能获取到手机号的，需要传入")
	private String phone = "";

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSourcePageType() {
		return sourcePageType;
	}

	public void setSourcePageType(String sourcePageType) {
		this.sourcePageType = sourcePageType;
	}

	
}
