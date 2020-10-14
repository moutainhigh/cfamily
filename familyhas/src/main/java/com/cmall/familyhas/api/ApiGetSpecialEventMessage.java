package com.cmall.familyhas.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetSpecialEventMessageInput;
import com.cmall.familyhas.api.result.ApiGetSpecialEventMessageResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取特殊活动滚动消息
 * @remark 
 * @author 任宏斌
 * @date 2020年3月17日
 */
public class ApiGetSpecialEventMessage extends RootApi<ApiGetSpecialEventMessageResult, ApiGetSpecialEventMessageInput>{

	@Override
	public ApiGetSpecialEventMessageResult Process(ApiGetSpecialEventMessageInput inputParam, MDataMap mRequestMap) {
		ApiGetSpecialEventMessageResult result = new ApiGetSpecialEventMessageResult();
		
		String sql = "SELECT nick_name FROM logcenter.lc_special_event_log ORDER BY create_time DESC LIMIT 100";
		List<Map<String, Object>> list = DbUp.upTable("sc_hudong_event_info").dataSqlList(sql, new MDataMap());
		if(null != list && list.size() > 0) {
			for (Map<String, Object> map : list) {
				result.getNickNameList().add(map.get("nick_name")+"");
			}
		}
		return result;
	}
	
}
