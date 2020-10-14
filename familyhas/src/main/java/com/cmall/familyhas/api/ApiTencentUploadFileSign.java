package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiTencentUploadFileSignInput;
import com.cmall.familyhas.api.result.ApiTencentUploadFileSignResult;
import com.cmall.familyhas.service.LiveService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;

public class ApiTencentUploadFileSign extends RootApi<ApiTencentUploadFileSignResult, ApiTencentUploadFileSignInput> {

	@Override
	public ApiTencentUploadFileSignResult Process(ApiTencentUploadFileSignInput inputParam, MDataMap mRequestMap) {
		ApiTencentUploadFileSignResult result = new ApiTencentUploadFileSignResult();
		String expireTime = inputParam.getExpireTime();
		String uploadVideo = new LiveService().uploadVideo(null, null, Integer.parseInt(expireTime));
		result.setSign(uploadVideo);
		return result;
	}

}
