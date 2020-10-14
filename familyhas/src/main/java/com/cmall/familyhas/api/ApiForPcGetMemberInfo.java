package com.cmall.familyhas.api;

import com.cmall.membercenter.model.HomePoolGetMemberResult;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 
 * 获取用户信息
 * 
 * @author dyc
 * 
 */
public class ApiForPcGetMemberInfo extends RootApiForToken<HomePoolGetMemberResult, RootInput> {

	public HomePoolGetMemberResult Process(RootInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		// return null;

		MemberLoginSupport memberLoginSupport = new MemberLoginSupport();

		HomePoolGetMemberResult getInfoResult = memberLoginSupport
				.HomePoolGetMemberInfo(getUserCode());

		return getInfoResult;
	}

}
