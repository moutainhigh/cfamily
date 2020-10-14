package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * @author fq
 *
 */
public class ApiProductToBuyResult extends RootResultWeb{
	
	
	@ZapcomApi(value="订单下所有商品的购买状态",require=1,remark="0：全部成功;1：部分成功，部分失败（商品失效会失败）;2:全部失败")
	private Integer result_status = 0;

	public Integer getResult_status() {
		return result_status;
	}

	public void setResult_status(Integer result_status) {
		this.result_status = result_status;
	}
	
	

}
