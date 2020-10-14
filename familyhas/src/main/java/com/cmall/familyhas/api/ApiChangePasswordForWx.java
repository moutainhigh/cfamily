package com.cmall.familyhas.api;

import com.cmall.membercenter.model.ChangePasswordForWxInput;
import com.cmall.membercenter.model.ChangePasswordInput;
import com.cmall.membercenter.support.MemberInfoSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;


/** 
* @ClassName: ApiChangePasswordForWx 
* @Description: 修改密码，共微信商城调用
* @author 张海生
* @date 2015-4-3 下午3:13:13 
*  
*/
public class ApiChangePasswordForWx extends
		RootApiForToken<RootResultWeb, ChangePasswordForWxInput> {

	public RootResultWeb Process(ChangePasswordForWxInput inputParam,
			MDataMap mRequestMap) {
		
		return new MemberInfoSupport().changePasswordForWx(getUserCode(), inputParam);
	}

}
