package com.cmall.familyhas.api;


import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiForValidateWeChatBoundInput;
import com.cmall.familyhas.api.result.ApiForValidateWeChatBoundResult;
import com.cmall.systemcenter.util.AESUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;



public class ApiForValidateWeChatBound extends RootApiForVersion<ApiForValidateWeChatBoundResult,  ApiForValidateWeChatBoundInput>{

	@Override
	public ApiForValidateWeChatBoundResult Process(ApiForValidateWeChatBoundInput inputParam, MDataMap mRequestMap) {
		ApiForValidateWeChatBoundResult rootResult = new ApiForValidateWeChatBoundResult();
		String uid = inputParam.getUid();
		MDataMap one = DbUp.upTable("oc_coupon_wechat_validate").one("uid",uid);
		if(one==null||StringUtils.equals(one.get("flag"), "1")) {
			rootResult.setResultCode(0);
			rootResult.setResultMessage("链接已失效!");
			return rootResult;
		}
		AESUtil aesUtil = new AESUtil();
		aesUtil.initialize();
		MDataMap one2 = DbUp.upTable("mc_member_wechat_bound").one("member_code",one.get("member_code"));
		String phone_num = aesUtil.encrypt(one2.get("phone_num"));
		rootResult.setPhone(phone_num);
		rootResult.setValidate_code(one.get("validate_code"));
		return rootResult;
	}

}
