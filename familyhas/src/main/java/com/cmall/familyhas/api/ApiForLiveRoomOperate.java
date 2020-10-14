package com.cmall.familyhas.api;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForLiveRoomOperateInput;
import com.cmall.familyhas.api.result.ApiForLiveRoomOperateResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 直播间观看/点赞/评论接口
 * @author lgx
 *
 */
public class ApiForLiveRoomOperate extends RootApiForVersion<ApiForLiveRoomOperateResult, ApiForLiveRoomOperateInput> {

	@Override
	public ApiForLiveRoomOperateResult Process(ApiForLiveRoomOperateInput inputParam, MDataMap mRequestMap) {
		ApiForLiveRoomOperateResult result = new ApiForLiveRoomOperateResult();
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String liveRoomId = inputParam.getLiveRoomId();
		String behaviorType = inputParam.getBehaviorType();
		
		String ifStopComment = "0";
		
		MDataMap liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId,"is_delete","0");
		if(liveRoom != null) {
			if("449748630001".equals(behaviorType)) {
				// 观看不要求登录,所有不登录观看数记录在空的member_code下
				// 观看,先查询用户是否观看过该直播间
				MDataMap one = DbUp.upTable("lv_live_room_behavior_statistics").one("live_room_id",liveRoomId,"member_code",memberCode,"behavior_type", "449748630001");
				if(one != null) {
					// 观看过,更新用户观看次数
					int num = MapUtils.getIntValue(one, "num");
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("uid", MapUtils.getString(one, "uid"));
					mDataMap.put("num", num+1+"");
					DbUp.upTable("lv_live_room_behavior_statistics").dataUpdate(mDataMap, "num", "uid");
				}else {
					// 没观看过,记录用户观看记录
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("live_room_id", liveRoomId);
					mDataMap.put("member_code", memberCode);
					mDataMap.put("behavior_type", "449748630001");
					mDataMap.put("num", "1");
					DbUp.upTable("lv_live_room_behavior_statistics").dataInsert(mDataMap);
				}
			}else if("449748630002".equals(behaviorType)) {
				if(StringUtils.isNotBlank(memberCode)) {
					// 点赞,先查询用户是否点赞过该直播间
					MDataMap one = DbUp.upTable("lv_live_room_behavior_statistics").one("live_room_id",liveRoomId,"member_code",memberCode,"behavior_type", "449748630002");
					if(one != null) {
						// 点赞过,更新用户点赞次数
						int num = MapUtils.getIntValue(one, "num");
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("uid", MapUtils.getString(one, "uid"));
						mDataMap.put("num", num+1+"");
						DbUp.upTable("lv_live_room_behavior_statistics").dataUpdate(mDataMap, "num", "uid");
					}else {
						// 没点赞过,记录用户点赞记录
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("live_room_id", liveRoomId);
						mDataMap.put("member_code", memberCode);
						mDataMap.put("behavior_type", "449748630002");
						mDataMap.put("num", "1");
						DbUp.upTable("lv_live_room_behavior_statistics").dataInsert(mDataMap);
					}
				} else {
					result.setResultCode(969905922);
					result.setResultMessage(bInfo(969905922));
				}
			}else if("449748630003".equals(behaviorType)) {
				if(StringUtils.isNotBlank(memberCode)) {
					// 评论,先查询用户是否评论过该直播间
					MDataMap one = DbUp.upTable("lv_live_room_behavior_statistics").one("live_room_id",liveRoomId,"member_code",memberCode,"behavior_type", "449748630003");
					if(one != null) {
						// 评论过,更新用户评论次数
						int num = MapUtils.getIntValue(one, "num");
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("uid", MapUtils.getString(one, "uid"));
						mDataMap.put("num", num+1+"");
						DbUp.upTable("lv_live_room_behavior_statistics").dataUpdate(mDataMap, "num", "uid");
					}else {
						// 没评论过,记录用户评论记录
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("live_room_id", liveRoomId);
						mDataMap.put("member_code", memberCode);
						mDataMap.put("behavior_type", "449748630003");
						mDataMap.put("num", "1");
						DbUp.upTable("lv_live_room_behavior_statistics").dataInsert(mDataMap);
					}
				} else {
					result.setResultCode(969905922);
					result.setResultMessage(bInfo(969905922));
				}
			}
			
			ifStopComment = MapUtils.getString(liveRoom, "if_stop_comment").equals("")?"0":MapUtils.getString(liveRoom, "if_stop_comment");
		}else {
			ifStopComment = "1";
			result.setResultCode(-1);
			result.setResultMessage("该直播间不存在!");
		}
		
		result.setIfStopComment(ifStopComment);
		
		return result;
	}

}
