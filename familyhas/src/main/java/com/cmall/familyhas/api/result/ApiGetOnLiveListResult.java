package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetOnLiveListResult extends RootResult{

	@ZapcomApi(value="直播列表json",remark="中转直播的接口")
	private String liveListJson = "";

	/**
	 * 返回: the liveListJson <br>
	 * 
	 * 时间: 2016-8-2 下午3:46:55
	 */
	public String getLiveListJson() {
		return liveListJson;
	}

	/**
	 * 参数: liveListJson the liveListJson to set <br>
	 * 
	 * 时间: 2016-8-2 下午3:46:55
	 */
	public void setLiveListJson(String liveListJson) {
		this.liveListJson = liveListJson;
	}
	
	
	
}
