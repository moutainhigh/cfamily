package com.cmall.familyhas.api;


import com.cmall.familyhas.api.input.ApiKJTOrderOutInfoInput;
import com.cmall.familyhas.api.result.ApiKJTOrderOutInfoResult;
import com.cmall.familyhas.service.KJTOrderOutInfoService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;

/**
 * 提供给跨境通（KJT）
 * KJT通知分销渠道订单已出关区,判断出关是否成功，若成功，则将KJT订单编号
 * 物流公司编号、订单物流编号、出关失败的原因同步到惠家有系统
 * @author pangjh
 *
 */
public class ApiKJTOrderOutInfo extends RootApi<ApiKJTOrderOutInfoResult, ApiKJTOrderOutInfoInput> {

	public ApiKJTOrderOutInfoResult Process(ApiKJTOrderOutInfoInput inputParam,
			MDataMap mRequestMap) {
		
		return new KJTOrderOutInfoService().doProcess(inputParam,mRequestMap);
		
	}

}
