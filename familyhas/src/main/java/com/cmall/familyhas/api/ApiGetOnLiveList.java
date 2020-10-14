package com.cmall.familyhas.api;

import org.apache.commons.codec.digest.DigestUtils;

import com.cmall.familyhas.api.result.ApiGetOnLiveListResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapweb.webapi.RootApiForManage;


/**
 * 
 * 类: ApiGetOnLiveList <br>
 * 描述: 获取直播列表 <br>
 * 作者: 付强 fuqiang@huijiayou.cn<br>
 * 时间: 2016-8-2 下午3:48:44
 */
public class ApiGetOnLiveList extends RootApiForManage<ApiGetOnLiveListResult, RootInput>{

	@Override
	public ApiGetOnLiveListResult Process(RootInput inputParam, MDataMap mRequestMap) {
		
		ApiGetOnLiveListResult result = new ApiGetOnLiveListResult();
		WebClientSupport support = new WebClientSupport();
		try {
			//中转直播项目的直播列表接口

			String sResponseString = support.doGet(bConfig("cfamily.liveListUrl"));
			result.setLiveListJson(sResponseString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
