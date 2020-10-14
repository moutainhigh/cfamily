package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @ClassName: UserRegForWxInput 
* @Description: 微信商城以及自己的系统调用惠家友注册输入实体
* @author 张海生
* @date 2015-4-2 上午11:32:29 
*  
*/
public class UserRegForWxInput extends RootInput  {

	@ZapcomApi(value = "手机号", require = 1, remark = "手机号", demo = "13388888888", verify = "base=mobile")
	private String mobile = "";
	
	@ZapcomApi(value = "用户密码", require = 1, demo = "123456", remark = "用户的密码，支持特殊字符。")
	private String password = "";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
