package com.cmall.familyhas.api;

import java.text.SimpleDateFormat;

import com.cmall.familyhas.api.result.ApiForGetServerTimeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;


/** 
* @ClassName: ApiForGetServerTime 
* @Description: 获取系统时间
* @author 张海生
* @date 2015-5-15 下午3:46:19 
*  
*/
public class ApiForGetServerTime extends
		RootApi<ApiForGetServerTimeResult, RootInput> {

	public ApiForGetServerTimeResult Process(RootInput input,MDataMap mRequestMap) { 
		
		ApiForGetServerTimeResult result = new ApiForGetServerTimeResult();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = df.format(new java.util.Date());
		result.setServerTime(nowTime);
		return result;
	}
}
