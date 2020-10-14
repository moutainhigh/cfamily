package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.UserRegForWxInput;
import com.cmall.membercenter.model.MLoginInput;
import com.cmall.membercenter.model.MReginsterResult;
import com.cmall.membercenter.txservice.TxMemberForWx;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * @ClassName:
 * @Description: 注册(供微信商城调用)
 * @author 张海生
 * @date 2015-4-2 上午10:48:28
 * 
 */
public class ApiUserReginsterForWx extends
		RootApiForManage<MReginsterResult, UserRegForWxInput> {

	public MReginsterResult Process(UserRegForWxInput inputParam,
			MDataMap mRequestMap) {

		MReginsterResult userRegResult = new MReginsterResult();
		MLoginInput input = new MLoginInput();
		String mobile = inputParam.getMobile();
		String pwd = inputParam.getPassword();
		TxMemberForWx memberService = BeansHelper
				.upBean("bean_com_cmall_membercenter_txservice_TxMemberForWx");
		input.setLoginName(mobile);
		input.setLoginPassword("");
		input.setManageCode(getManageCode());
		userRegResult = memberService.insertUserReg(input);//注册用户
		if (userRegResult.upFlagTrue()) {// 注册成功
			RootResultWeb webResult = memberService.changePassword(// 把密码改为用户传的密码
					userRegResult.getMemberInfo().getMemberCode(), pwd);
			if (!webResult.upFlagTrue()) {
				userRegResult.inErrorMessage(webResult.getResultCode());
			}
		}
		return userRegResult;
	}
}
