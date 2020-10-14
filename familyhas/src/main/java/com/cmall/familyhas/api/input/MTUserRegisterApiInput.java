package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @ClassName: MTUserRegisterApiInput 
* @Description: MT管家输入实体
* @author 张海生
* @date 2015-10-21 下午5:20:17 
*  
*/
public class MTUserRegisterApiInput extends RootInput {

	@ZapcomApi(value = "手机号", remark = "手机号", require=1, demo = "18801256845" , verify = "base=mobile")
	private String mobile = "";
	
	@ZapcomApi(value = "密码", remark = "密码", require=1)
	private String passWord = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
	
}
