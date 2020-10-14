package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForLiveRoomStartInput;
import com.cmall.familyhas.api.result.ApiForLiveRoomStartResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 直播开始推流接口
 * 主播开启直播后，前端调用此接口，变更房间状态为：直播中,调用腾讯云
 * @author lgx
 *
 */
public class ApiForLiveRoomStart extends RootApiForVersion<ApiForLiveRoomStartResult, ApiForLiveRoomStartInput> {

	@Override
	public ApiForLiveRoomStartResult Process(ApiForLiveRoomStartInput inputParam, MDataMap mRequestMap) {
		ApiForLiveRoomStartResult result = new ApiForLiveRoomStartResult();
		String liveRoomId = inputParam.getLiveRoomId();
		
		MDataMap liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId,"is_delete","0");
		if(liveRoom != null) {
			String live_status = liveRoom.get("live_status");
			if("449746320001".equals(live_status)) {
				// 未开始的直播状态改成直播中
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("live_room_id", liveRoomId);
				mDataMap.put("live_status", "449746320003");
				DbUp.upTable("lv_live_room").dataUpdate(mDataMap, "live_status", "live_room_id");
				
				// 记录开播时间
				MDataMap mDataMap1 = new MDataMap();
				mDataMap1.put("live_room_id", liveRoomId);
				mDataMap1.put("start_time", FormatHelper.upDateTime());
				DbUp.upTable("lc_live_room_log").dataInsert(mDataMap1);
			}
		}else {
			result.setResultCode(-1);
			result.setResultMessage("该直播间不存在!");
		}
		
		return result;
	}

}
