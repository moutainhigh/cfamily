package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/***
 * 根据区域查询支付方式输出参数
 * @author jlin
 *
 */
public class ApiForGetPayTypeResult extends RootResultWeb {

	@ZapcomApi(value = "支付方式编码",demo="449716200001", remark = "449716200001:在线支付  449716200002:货到付款")
	private  String pay_type ="";

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
}
