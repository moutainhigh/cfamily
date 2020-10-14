package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 推送设备信息输入参数
 * @author fq
 *
 */
public class InitMyCollectionInput extends RootInput{

	@ZapcomApi(value="调用接口输入密码",require=1)
	private String pwd = "";

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
}
