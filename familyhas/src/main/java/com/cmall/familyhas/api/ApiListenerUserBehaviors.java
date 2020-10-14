package com.cmall.familyhas.api;



import com.cmall.familyhas.api.input.ApiListenerUserBehaviorsInput;
import com.cmall.familyhas.api.result.ApiListenerUserBehaviorsResult;
import com.cmall.familyhas.service.UserBehaviorsService;
import com.cmall.systemcenter.common.CouponConst;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiListenerUserBehaviors extends RootApiForVersion<ApiListenerUserBehaviorsResult, ApiListenerUserBehaviorsInput> {

	
	@Override
	public ApiListenerUserBehaviorsResult Process(ApiListenerUserBehaviorsInput inputParam, MDataMap mRequestMap) {
	
		UserBehaviorsService behaviorsService = new UserBehaviorsService();		
		ApiListenerUserBehaviorsResult recordUsersBeahviors = new ApiListenerUserBehaviorsResult();
		if(getFlagLogin()) {
			 recordUsersBeahviors = behaviorsService.recordUsersBeahviors(CouponConst.start_up_coupon,  getOauthInfo().getUserCode(), getOauthInfo().getLoginName(),  getManageCode());
		}
		return recordUsersBeahviors;		
	}

}
