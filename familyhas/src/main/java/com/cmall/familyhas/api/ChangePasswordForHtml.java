package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ChangePasswordForHtmlInput;
import com.cmall.membercenter.model.ChangePasswordInput;
import com.cmall.membercenter.support.MemberInfoSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 修改密码
 * 
 * @author xgj
 * 
 */
public class ChangePasswordForHtml extends
		RootApiForToken<RootResultWeb, ChangePasswordForHtmlInput> {

	public RootResultWeb Process(ChangePasswordForHtmlInput inputParam,
			MDataMap mRequestMap) {

		MemberInfoSupport memberInfoSupport = new MemberInfoSupport();
		ChangePasswordInput changePasswordInput = new ChangePasswordInput();
		changePasswordInput.setNew_password(inputParam.getNew_password());
		changePasswordInput.setOld_password("");
		return memberInfoSupport.changePasswordForHtml(getUserCode(), changePasswordInput);
	}

}
