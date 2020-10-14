package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiGetRecommendConfigInput;
import com.cmall.familyhas.api.result.ApiGetRecommendConfigResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiGetRecommendConfig extends RootApiForVersion<ApiGetRecommendConfigResult, ApiGetRecommendConfigInput> {

	@Override
	public ApiGetRecommendConfigResult Process(ApiGetRecommendConfigInput inputParam, MDataMap mRequestMap) {
		ApiGetRecommendConfigResult apiResult = new ApiGetRecommendConfigResult();
		apiResult.setUpConfig(bConfig("familyhas.recommend_up_config"));
		apiResult.setGetConfig(bConfig("familyhas.recommend_get_config"));
		return apiResult;
	}
}
