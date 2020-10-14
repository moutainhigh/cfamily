package com.cmall.familyhas.api;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetLiveRoomInfoInput;
import com.cmall.familyhas.api.result.ApiForGetLiveRoomInfoResult;
import com.cmall.familyhas.service.LiveService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 根据直播房间编号获取房间信息接口
 * @author lgx
 *
 */
public class ApiForGetLiveRoomInfo extends RootApiForVersion<ApiForGetLiveRoomInfoResult, ApiForGetLiveRoomInfoInput> {

	final long expire = 24*60*60;
	
	private LiveService liveService = new LiveService();
	
	@Override
	public ApiForGetLiveRoomInfoResult Process(ApiForGetLiveRoomInfoInput inputParam, MDataMap mRequestMap) {
		ApiForGetLiveRoomInfoResult result = new ApiForGetLiveRoomInfoResult();
		String channelId = getChannelId();
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String anchorOrUser = inputParam.getAnchorOrUser();
		String liveRoomId = inputParam.getLiveRoomId();
		String fileId = inputParam.getFileId();
		result.setAnchorOrUser(anchorOrUser);
		//LiveService liveService = new LiveService();
		// 主播用户编码
		String liveMemberCode = "";
		
		MDataMap liveRoom = new MDataMap();
		if("1".equals(anchorOrUser)) {
			// 用户进来,看没有直播间编号,有的话说明是客户点击查看直播;
			if(StringUtils.isNotBlank(liveRoomId)) {				
				// 客户点击查看直播
				liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId,"is_delete","0");
				if(liveRoom == null) {
					result.setResultCode(-1);
					result.setResultMessage("该直播间不存在!");
					return result;
				}
				String loginSql = "SELECT member_code FROM mc_login_info WHERE login_name = '"+liveRoom.get("anchor_phone")+"' AND manage_code = 'SI2003' ";
				Map<String, Object> dataSqlOne = DbUp.upTable("mc_login_info").dataSqlOne(loginSql, new MDataMap());
				if(dataSqlOne != null) {
					liveMemberCode = MapUtils.getString(dataSqlOne, "member_code");
				}
			}
		}else {
			// 主播入口进来,应该是主播进入直播间,查询一个时间最合适的直播间返回(时间最接近未结束)
			if("449747430001".equals(channelId) && StringUtils.isNotBlank(memberCode)) {
				// 主播进入直播间,查询一个时间最合适的直播间返回(时间最接近未结束)
				Map<String, Object> loginNameMap = DbUp.upTable("mc_login_info").dataSqlOne("SELECT login_name FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003' ", new MDataMap());
				if(loginNameMap != null) {
					String login_name = MapUtils.getString(loginNameMap, "login_name");
					String liveSql = "SELECT * FROM lv_live_room WHERE anchor_phone = '"+login_name+"' AND is_delete = '0' AND live_status != '449746320002'  ORDER BY start_time LIMIT 1";
					Map<String, Object> dataSqlOne = DbUp.upTable("lv_live_room").dataSqlOne(liveSql, new MDataMap());
					if(dataSqlOne != null && dataSqlOne.size() > 0) {
						// 登录的手机号在主播列表有未结束的直播间
						liveRoom = new MDataMap(dataSqlOne);
					}else {
						result.setResultCode(-1);
						result.setResultMessage("主播的直播间都已结束!");
						return result;
					}
				}
			}
		}
		
		if(liveRoom != null && liveRoom.size() > 0) {
			liveRoomId = liveRoom.get("live_room_id");
			
			String nickName = liveRoom.get("anchor_nickname");
			String avatar = liveRoom.get("anchor_avatar");
			String liveCoverPicture = liveRoom.get("live_cover_picture");
			String liveBackgroundPicture = liveRoom.get("live_background_picture");
			String liveFunctions = liveRoom.get("live_functions");
			String liveStatus = liveRoom.get("live_status");
			String startTime = liveRoom.get("start_time");
			String liveTitle = liveRoom.get("live_title");
			String groupId = liveRoom.get("group_id");
			if(StringUtils.isBlank(groupId)) {
				// 获取群组号,群组名称为直播间编号
				groupId = liveService.createGroup("", "", "", expire, liveRoomId);
				// 因为数据库没有存群组号,需要入库
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("live_room_id", liveRoomId);
				mDataMap.put("group_id", groupId);
				DbUp.upTable("lv_live_room").dataUpdate(mDataMap, "group_id", "live_room_id");
			}
			//int approveNum = 0;
			//int evaluationNum = 0;
			// 推流地址
			Date date = new Date();
			long time = date.getTime()/1000;
			String livePushUrl = liveService.getPushUrl("", "", liveRoomId, time+expire);
			// 拉流地址
			String livePullUrl = liveService.getPullUrl("", "", liveRoomId, time+expire);
			String userSig = liveService.genSig("", memberCode, expire, "");
			
			String liveFileUrl = "";
			if("449746320002".equals(liveStatus)) {
				if(StringUtils.isNotBlank(fileId)) {					
					// 直播结束后才会有回放地址,根据前端所传文件fileId查
					Map<String, Object> replayMap = DbUp.upTable("lv_live_room_replay_infos").dataSqlOne("SELECT video_url FROM lv_live_room_replay_infos WHERE live_room_id = '"+liveRoomId+"' AND file_id = '"+fileId+"'", new MDataMap());
					if(replayMap != null) {
						liveFileUrl = MapUtils.getString(replayMap, "video_url");
					}
				}else {
					// 前端没传文件fileId,只查询一条
					Map<String, Object> replayMap = DbUp.upTable("lv_live_room_replay_infos").dataSqlOne("SELECT video_url FROM lv_live_room_replay_infos WHERE live_room_id = '"+liveRoomId+"' limit 1", new MDataMap());
					if(replayMap != null) {
						liveFileUrl = MapUtils.getString(replayMap, "video_url");
					}
				}
			}
			
			// 晒单人头像
			String userAvatar = "";
			// 晒单人昵称
			String userNickName = "";
			if(StringUtils.isNotBlank(memberCode)) {
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"' ORDER BY last_update_time DESC LIMIT 1", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003'", new MDataMap());
					userNickName = (String) login_info.get("login_name");
					if(isPhone(userNickName)) {
						userNickName = userNickName.substring(0, 3) + "****" + userNickName.substring(7);
					}
				}else { // 如果昵称不是空
					userNickName = (String) member_sync.get("nickname");
				}
				// 头像
				if(null != member_sync && null != member_sync.get("avatar") && !"".equals(member_sync.get("avatar"))){
					userAvatar = (String) member_sync.get("avatar");
				}
			}
			
			result.setUserNickName(userNickName);
			result.setUserAvatar(userAvatar);
			result.setLiveMemberCode(liveMemberCode);
			result.setLiveRoomId(liveRoomId);
			result.setAvatar(avatar);
			result.setLiveBackgroundPicture(liveBackgroundPicture);
			result.setLiveCoverPicture(liveCoverPicture);
			result.setLiveFunctions(liveFunctions);
			result.setLiveStatus(liveStatus);
			result.setLiveTitle(liveTitle);
			result.setNickName(nickName);
			result.setStartTime(startTime);
			result.setGroupId(groupId);
			//result.setEvaluationNum(evaluationNum);
			//result.setApproveNum(approveNum);
			result.setLivePushUrl(livePushUrl);
			result.setLivePullUrl(livePullUrl);
			result.setLiveFileUrl(liveFileUrl);
			result.setUserSig(userSig);
		}
		
		return result;
	}

	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}
	
}
