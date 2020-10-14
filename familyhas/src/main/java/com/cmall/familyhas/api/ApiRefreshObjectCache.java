package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiRefreshObjectCacheInput;
import com.cmall.familyhas.api.result.ApiRefreshObjectCacheResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webdo.ObjectCache;

/**
 * 刷新ehcahe object 缓存
 * @author pang_jhui
 *
 */
public class ApiRefreshObjectCache extends RootApiForMember<ApiRefreshObjectCacheResult, ApiRefreshObjectCacheInput> {

	public ApiRefreshObjectCacheResult Process(ApiRefreshObjectCacheInput inputParam, MDataMap mRequestMap) {
			
		ObjectCache.getInstance().removeAll();
		
		ApiRefreshObjectCacheResult result = new ApiRefreshObjectCacheResult();
		
		result.setResultMessage("缓存刷新成功");

		return result;
	}

}
