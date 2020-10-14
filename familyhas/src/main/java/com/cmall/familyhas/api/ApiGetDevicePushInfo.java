package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiGetDevicePushInfoInput;
import com.cmall.familyhas.api.result.ApiGetDevicePushInfoResult;
import com.cmall.familyhas.service.DevicePushService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 查询手机号对应的设备信息
 * @author fq
 *
 */
public class ApiGetDevicePushInfo extends RootApiForManage<ApiGetDevicePushInfoResult, ApiGetDevicePushInfoInput>{

	@Override
	public ApiGetDevicePushInfoResult Process(
			ApiGetDevicePushInfoInput inputParam, MDataMap mRequestMap) {
		
		String[] splitMobiles = inputParam.getMobile().split(",");
		StringBuffer sBuffer = new StringBuffer();
		for (String mobile : splitMobiles) {
			sBuffer.append("'");
			sBuffer.append(mobile);
			sBuffer.append("'");
			sBuffer.append(",");
		}
		String moblies = "";
		if(sBuffer.length() >0) {
			moblies = sBuffer.substring(0, sBuffer.length()-1);
		}
		
		ApiGetDevicePushInfoResult result = new ApiGetDevicePushInfoResult();
		result.setDeviceList(DevicePushService.getDevicesByMobile(moblies));
		
		return result;
		
	}
}
