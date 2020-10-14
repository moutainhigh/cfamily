package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiUserIdentityInfoInput;
import com.cmall.familyhas.api.result.ApiUserIdentityInfoResult;
import com.cmall.familyhas.service.UserIdentityInfoService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户身份信息处理接口
 * @author pang_jhui
 *
 */
public class ApiUserIdentityInfo extends RootApiForToken<ApiUserIdentityInfoResult, ApiUserIdentityInfoInput> {

	public ApiUserIdentityInfoResult Process(ApiUserIdentityInfoInput inputParam, MDataMap mRequestMap) {
		
		if(mRequestMap == null){
			
			mRequestMap = new MDataMap();
			
		}
		
		mRequestMap.put("userCode", getUserCode());
		
		return new UserIdentityInfoService().doProcess(inputParam, mRequestMap);
		
	}

	

}
