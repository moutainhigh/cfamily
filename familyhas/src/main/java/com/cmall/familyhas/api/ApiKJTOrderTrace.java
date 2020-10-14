package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiKJTOrderTraceInput;
import com.cmall.familyhas.api.result.ApiKJTOrderTraceResult;
import com.cmall.familyhas.service.KJTOrderTraceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 跨境通（KJT）订单轨迹信息
 * @author pangjh
 *
 */
public class ApiKJTOrderTrace extends RootApiForToken<ApiKJTOrderTraceResult, ApiKJTOrderTraceInput> {

	public ApiKJTOrderTraceResult Process(ApiKJTOrderTraceInput inputParam,
			MDataMap mRequestMap) {
		
		return new KJTOrderTraceService().doProcess(inputParam);
	}

}
