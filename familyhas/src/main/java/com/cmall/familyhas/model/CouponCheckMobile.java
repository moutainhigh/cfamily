package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.baseclass.BaseClass;


/** 
* @ClassName: CouponCheckMobile 
* @Description: 方法账户
* @author 张海生
* @date 2016-1-19 下午3:58:14 
*  
*/
public class CouponCheckMobile  extends BaseClass{
	
	@ZapcomApi(value="手机号",remark="手机号")
	private String mobile="";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}

