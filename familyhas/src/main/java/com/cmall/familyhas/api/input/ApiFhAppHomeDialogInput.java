package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * @descriptions 
 * 
 * @refactor 
 * @author Yangcl
 * @date 2016-5-5-下午5:38:44
 * @version 1.0.0
 */
public class ApiFhAppHomeDialogInput extends RootInput {
	
	// 仅在接口测试页面测试人员进行测试的时候传入，APP客户端无需传入此参数
	@ZapcomApi(value = "系统当前时间",remark="接口测试页面测试人员进行测试的时候传入，APP客户端无需传入此参数")
	private String curentTime = "";
	
	public String getCurentTime() {
		return curentTime;
	}

	public void setCurentTime(String curentTime) {
		this.curentTime = curentTime;
	}
	
	
	
}
