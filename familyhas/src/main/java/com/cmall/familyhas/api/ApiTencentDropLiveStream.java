package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiTencentDropLiveStreamInput;
import com.cmall.familyhas.api.result.ApiTencentDropLiveStreamResult;
import com.cmall.familyhas.service.LiveService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiTencentDropLiveStream extends RootApi<ApiTencentDropLiveStreamResult, ApiTencentDropLiveStreamInput>{

	@Override
	public ApiTencentDropLiveStreamResult Process(ApiTencentDropLiveStreamInput inputParam, MDataMap mRequestMap) {
		ApiTencentDropLiveStreamResult apiTencentDropLiveStreamResult = new ApiTencentDropLiveStreamResult();
		String live_room_id =  inputParam.getLive_room_id();
		MDataMap mWhereMap = new MDataMap("live_room_id",live_room_id);
		List<Map<String,Object>> dataSqlList = DbUp.upTable("lc_live_room_log").dataSqlList("select * from lc_live_room_log where live_room_id=:live_room_id", mWhereMap);
		List<Map<String,Object>> dataSqlListGroup = DbUp.upTable("lv_live_room").dataSqlList("select * from lv_live_room  where live_room_id=:live_room_id", mWhereMap);
		long time = 0l;
		if(dataSqlList != null && dataSqlList.size() > 0 && dataSqlListGroup != null && dataSqlListGroup.size() > 0) {
			new LiveService().dropLiveStream(null, live_room_id);
			String string = dataSqlList.get(0).get("start_time").toString();
			String group_id = dataSqlListGroup.get(0).get("group_id").toString();
			new LiveService().dropGroup(null, null, null, 0, group_id);
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("live_status", "449746320002");
			mDataMap.put("live_room_id", live_room_id);
			try {
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATETIME);
				String format = simpleDateFormat.format(date);
				mDataMap.put("end_time", format);
				time = (date.getTime() - simpleDateFormat.parse(string).getTime())/1000;
				mDataMap.put("live_time", String.valueOf(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbUp.upTable("lv_live_room").dataUpdate(mDataMap, "live_status,live_time,end_time", "live_room_id");
		}
		apiTencentDropLiveStreamResult.setKeeplivetime(String.valueOf(time));
		return apiTencentDropLiveStreamResult;
	}

}
