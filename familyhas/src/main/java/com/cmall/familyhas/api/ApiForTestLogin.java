package com.cmall.familyhas.api;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiForTestLoginInput;
import com.cmall.familyhas.api.result.ApiForTestLoginResult;
import com.cmall.membercenter.group.model.GroupLoginInput;
import com.cmall.membercenter.group.model.GroupLoginResult;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;
/***
 * 登录
 * @author xiegj
 *
 */
public class ApiForTestLogin extends RootApiForManage<ApiForTestLoginResult,ApiForTestLoginInput> {

	public ApiForTestLoginResult Process(ApiForTestLoginInput inputParam, MDataMap mRequestMap) {
		ApiForTestLoginResult result=new ApiForTestLoginResult();
		MemberLoginSupport ls = new MemberLoginSupport();
		GroupLoginInput in = new GroupLoginInput();
		in.setLoginName(inputParam.getLoginName());
		in.setLoginPass(inputParam.getLoginPass());
		GroupLoginResult re = ls.doGroupLogin(in, getManageCode());
		result.setResultCode(re.getResultCode());
		result.setUserToken(re.getUserToken());
		result.setResultMessage(re.getResultMessage());
		return result;
	}
}
