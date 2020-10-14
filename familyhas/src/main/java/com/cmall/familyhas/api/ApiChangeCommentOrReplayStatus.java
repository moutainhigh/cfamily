package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiChangeCommentOrReplayStatusInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiChangeCommentOrReplayStatus extends RootApi<RootResult, ApiChangeCommentOrReplayStatusInput> {

	public RootResult Process(ApiChangeCommentOrReplayStatusInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult result = new RootResult();
		String flag = inputParam.getFlag();
		String liveRoomId = inputParam.getLiveRoomId();
		MDataMap one = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId);
		if(one!=null) {
			if("commentFlag".equals(flag)) {
				String subflag = "";
				//当前状态
				if("0".equals(one.get("if_stop_comment"))) {
					subflag = "1";
					result.setResultCode(1);
					result.setResultMessage("关闭");
				}else {
					subflag="0";
					result.setResultCode(1);
					result.setResultMessage("开启");
				}
				DbUp.upTable("lv_live_room").dataUpdate(new MDataMap("live_room_id",liveRoomId,"if_stop_comment",subflag), "if_stop_comment", "live_room_id");
				
			}else if("replayFlag".equals(flag)){
				String subflag = "";
				//当前状态
				if("0".equals(one.get("if_stop_replay"))) {
					subflag = "1";
					result.setResultCode(1);
					result.setResultMessage("关闭");
				}else {
					subflag="0";
					result.setResultCode(1);
					result.setResultMessage("开启");
				}
				DbUp.upTable("lv_live_room").dataUpdate(new MDataMap("live_room_id",liveRoomId,"if_stop_replay",subflag), "if_stop_replay", "live_room_id");
			}
		}
		
		return result;
	}

	

		

}
