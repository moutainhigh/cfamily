package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiIsLiveUserResult extends RootResult{

	@ZapcomApi(value="是否是直播用户")
	private boolean isLiveUser = false;
	

	/**
	 * 返回: the isLiveUser <br>
	 * 
	 * 时间: 2016-8-2 下午3:53:21
	 */
	public boolean isLiveUser() {
		return isLiveUser;
	}

	/**
	 * 参数: isLiveUser the isLiveUser to set <br>
	 * 
	 * 时间: 2016-8-2 下午3:53:21
	 */
	public void setLiveUser(boolean isLiveUser) {
		this.isLiveUser = isLiveUser;
	}

}
