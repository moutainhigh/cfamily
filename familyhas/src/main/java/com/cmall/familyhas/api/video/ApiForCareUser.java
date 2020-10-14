package com.cmall.familyhas.api.video;

import com.cmall.familyhas.api.video.input.ApiForPersonCenterInput;
import com.cmall.familyhas.api.video.result.ApiForCareUserResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 关注用户接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiForCareUser extends RootApiForToken<ApiForCareUserResult,ApiForPersonCenterInput> {
	
	public ApiForCareUserResult Process(ApiForPersonCenterInput input, MDataMap mRequestMap) {
		
		ApiForCareUserResult result = new ApiForCareUserResult();
		String userCode = getUserCode();
		int cnt = DbUp.upTable("lv_concern_detail").dataCount("member_code = '"+userCode+"' and concern_usercode = '"+input.getMemberCode()+"'", null);
		if(cnt>0){
			DbUp.upTable("lv_concern_detail").dataDelete("member_code = '"+userCode+"' and concern_usercode = '"+input.getMemberCode()+"'", null, "");
			result.setStatus("1");
		}else{
			MDataMap insertMap = new MDataMap();
			insertMap.put("member_code", userCode);
			insertMap.put("concern_usercode", input.getMemberCode());
			DbUp.upTable("lv_concern_detail").dataInsert(insertMap);
			result.setStatus("0");
		}
		
		return result;
	}
}
