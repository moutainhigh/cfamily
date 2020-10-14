package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetFxrInput;
import com.cmall.familyhas.api.input.ApiRegIsUserInput;
import com.cmall.familyhas.api.result.ApiGetFxrResult;
import com.cmall.familyhas.api.result.ApiRegIsUserResult;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetFxrInfo extends RootApiForManage<ApiGetFxrResult ,ApiGetFxrInput>{

	public ApiGetFxrResult Process(ApiGetFxrInput inputParam,
			MDataMap mRequestMap) {
		ApiGetFxrResult result = new ApiGetFxrResult();
		MDataMap mp = new MDataMap();
		mp.put("manage_code", getManageCode());
		mp.put("login_name", inputParam.getWxCode());
		MDataMap dataCount = DbUp.upTable("mc_login_info").one("manage_code",getManageCode(),"login_name",inputParam.getWxCode());
		if(dataCount != null ) {
			String openid_xch = dataCount.get("openid_xch");
			if(StringUtils.isBlank(openid_xch)){
				result.setResultCode(-1);
				result.setResultMessage("该用户没有授权登录过惠家有特卖小程序");
				return result;
			}
			String member_code = dataCount.get("member_code");
			MDataMap nickNameInfo = DbUp.upTable("mc_member_sync").one("member_code",member_code);
			String nick_name = "匿名用户";
			if(nickNameInfo != null && !nickNameInfo.isEmpty()) {
				nick_name = DbUp.upTable("mc_member_sync").one("member_code",member_code).get("nickname");
			}
			result.setMemberCode(member_code);
			result.setNickName(nick_name);
			result.setMobile(dataCount.get("login_name"));
		}else{
			result.setResultCode(-1);
			result.setResultMessage("没有此用户");
		}
		return result;
	}

}
