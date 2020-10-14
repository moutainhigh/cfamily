package com.cmall.familyhas.api;


import com.cmall.familyhas.api.input.MTUserRegisterApiInput;
import com.cmall.familyhas.api.result.MTUserRegisterApiResult;
import com.cmall.groupcenter.weixin.WebchatConstants;
import com.cmall.membercenter.group.model.GroupLoginInput;
import com.cmall.membercenter.group.model.GroupLoginResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.membercenter.model.MLoginInput;
import com.cmall.membercenter.model.UserRegisterForGroupResult;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.membercenter.txservice.TxMemberForGroupService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * MT管家用户导入分别注册微公社和惠家有
 * 
 * @author zhanghs
 *
 */
public class MTUserRegisterApi extends
	RootApiForManage<MTUserRegisterApiResult, MTUserRegisterApiInput> {

	public MTUserRegisterApiResult Process(
			MTUserRegisterApiInput inputParam, MDataMap mRequestMap) {
		
		MTUserRegisterApiResult result = new MTUserRegisterApiResult();
		UserRegisterForGroupResult groupRegisterResult = new UserRegisterForGroupResult();
		TxMemberForGroupService memberService = BeansHelper
				.upBean("bean_com_cmall_membercenter_txservice_TxMemberForGroupService");
		MLoginInput mLoginInput = new MLoginInput();
		String mobile = inputParam.getMobile();
		String passWord = inputParam.getPassWord();
		mLoginInput.setLoginName(mobile);
		mLoginInput.setLoginGroup(MemberConst.LOGIN_GROUP_DEFAULT);
		mLoginInput.setLoginPassword(passWord);
		mLoginInput.setManageCode(WebchatConstants.CGROUP_MANAGE_CODE);
		//微公社注册
		groupRegisterResult = memberService.createMemberInfoForGroup(mLoginInput);
		if(groupRegisterResult.upFlagTrue()){
			MemberLoginSupport ls = new MemberLoginSupport();
			GroupLoginInput in = new GroupLoginInput();
			in.setLoginName(mobile);
			in.setLoginPass(passWord);
			GroupLoginResult re = ls.doGroupLogin(in, getManageCode());//登录惠家有
			result.setMemberCode(re.getMemberCode());
			result.inErrorMessage(re.getResultCode());
		}else{
			result.inErrorMessage(groupRegisterResult.getResultCode());
		}
		return result;
	}

	
}
