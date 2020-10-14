package com.cmall.familyhas.api;

import java.util.Map;

import com.cmall.familyhas.api.result.ApiForRedPackageEventResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 获取当前正在进行的抢红包活动编号
 * @author Angel Joy
 *
 */
public class ApiForRedPackageEvent extends RootApiForToken<ApiForRedPackageEventResult , RootInput > {

	@Override
	public ApiForRedPackageEventResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiForRedPackageEventResult result = new ApiForRedPackageEventResult();
		String sql = "SELECT * FROM systemcenter.sc_hudong_event_info WHERE begin_time <sysdate() AND end_time > sysdate() AND event_type_code = '449748210008' AND event_status = '4497472700020002' LIMIT 1";
		Map<String,Object> eventInfo = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sql, null);
		if(eventInfo == null) {
			result.setResultMessage("当前暂无活动");
			result.setResultCode(-1);
		}else {
			result.setEventCode((String)eventInfo.get("event_code"));
		}
		return result;
	}

}
