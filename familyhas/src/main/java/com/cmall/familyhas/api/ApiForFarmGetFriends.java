package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmGetFriendsInput;
import com.cmall.familyhas.api.model.Friend;
import com.cmall.familyhas.api.result.ApiForFarmGetFriendsResult;
import com.cmall.familyhas.service.FarmService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取好友列表接口
 * 返回全部好友
 * 偷过的人+可偷的好友>=10就不展示随机偷取
 * 展示随机偷取时,返回一个随机memberCode
 * @author lgx
 *
 */
public class ApiForFarmGetFriends extends RootApiForVersion<ApiForFarmGetFriendsResult, ApiForFarmGetFriendsInput> {

	private FarmService farmService = new FarmService();
	
	@Override
	public ApiForFarmGetFriendsResult Process(ApiForFarmGetFriendsInput inputParam, MDataMap mRequestMap) {
		ApiForFarmGetFriendsResult result = new ApiForFarmGetFriendsResult();

		String randomFlag = "0";
		String randomMemberCode = "";
		List<Friend> friendList = new ArrayList<Friend>();
		
		// 如果登录了,获取登录人memberCode
		String memberCode = "";
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
		}else {
			result.setResultCode(-1);
			result.setResultMessage("请您先登录!");
			return result;
		}
		
		String nowTime = FormatHelper.upDateTime();
		String begin = nowTime.substring(0, 10)+" 00:00:00";
		String end = nowTime.substring(0, 10)+" 23:59:59";
		// 查询当前时间段内已经发布状态的惠惠农场活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的惠惠农场活动!");
			return result;
		}else {
			String event_code = (String) eventInfoMap.get("event_code");
			// 先看今天偷过几个好友
			String countSql = "SELECT count(DISTINCT(stolen_code)) num FROM sc_huodong_farm_user_sneak_log WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND stolen_code != '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
			Map<String, Object> countMap = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(countSql, new MDataMap());
			int count = 0;
			if(null != countMap) {
				count = MapUtils.getIntValue(countMap,"num");
			}
			// 展示随机偷取的限制好友数
			MDataMap friendNumMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480015");
			int friendNum = MapUtils.getIntValue(friendNumMap,"begin_num");
			// 查询所有好友
			String friendSql = "SELECT DISTINCT(friend_code) friend_code FROM sc_huodong_farm_user_friend WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"'";
			List<Map<String, Object>> userFriendList = DbUp.upTable("sc_huodong_farm_user_friend").dataSqlList(friendSql, new MDataMap());
			if(null != userFriendList && userFriendList.size() > 0) {
				// 可偷取好友+已偷取好友
				int stealFriendNum = 0;
				
				// 循环判断是否有可以偷取的水滴
				for (Map<String, Object> friendMap : userFriendList) {
					Friend friend = new Friend();
					// 是否可偷取标识:0否 ; 1是
					String stealFlag = "0";
					String nickName = "";
					String avatar = "";
					String friend_code = (String) friendMap.get("friend_code");
					// 好友水滴
					String userWaterSql = "SELECT * FROM sc_huodong_farm_user_water WHERE member_code = '"+friend_code+"' AND event_code = '"+event_code+"' AND flag = '1' ORDER BY create_time DESC";
					List<Map<String, Object>> userWaterList = DbUp.upTable("sc_huodong_farm_user_water").dataSqlList(userWaterSql, new MDataMap());
					if(null != userWaterList && userWaterList.size() > 0) {
						for (Map<String, Object> userWaterMap : userWaterList) {
							// 循环判断是否有可以偷取的水滴,只要有一个就是可偷取,跳出循环
							String water_code = (String) userWaterMap.get("water_code");
							int water_num = (int) userWaterMap.get("water_num");
							// 水滴保底克数
							MDataMap waterEndMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480014");
							int endWater = MapUtils.getIntValue(waterEndMap,"begin_num");
							// 水滴剩余大于保底克数
							if(water_num > endWater) {
								// 看今天有没有偷过这个水滴
								String sneakLogSql = "SELECT * FROM sc_huodong_farm_user_sneak_log WHERE stolen_water_code = '"+water_code+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
								Map<String, Object> sneakLogMap = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(sneakLogSql, new MDataMap());
								if(null != sneakLogMap) {
									// 有偷取记录,则不可偷取
									stealFlag = "0";
								}else {
									// 没有偷取记录,则可偷取
									stealFlag = "1";
								}
							}
							// 只要有一个就是可偷取,跳出循环
							if("1".equals(stealFlag)) {
								// 可偷取好友+1
								stealFriendNum++;
								break;
							}
						}
					}
					
					// 如果该好友不可偷取,看今天是否偷取过该好友
					String sneakSql = "SELECT count(1) num FROM sc_huodong_farm_user_sneak_log WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND stolen_code = '"+friend_code+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
					Map<String, Object> sneak = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(sneakSql, new MDataMap());
					int sneakCount = 0;
					if(null != sneak) {
						sneakCount = MapUtils.getIntValue(sneak,"num");
					}
					if("0".equals(stealFlag)) {
						// 如果该好友不可偷取,看今天是否偷取过该好友
						if(sneakCount > 0) {
							// 已经偷取好友+1
							stealFriendNum++;
						}
					}
					
					// 查询别人昵称
					Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+friend_code+"'", new MDataMap());
					if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
						// 如果昵称是空,查询手机号
						Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+friend_code+"'", new MDataMap());
						if(login_info != null) {
							nickName = (String) login_info.get("login_name");
							if(farmService.isPhone(nickName)) {
								nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
							}
						}else {
							continue;
						}
					}else { // 如果昵称不是空
						nickName = (String) member_sync.get("nickname");
					}
					// 头像
					if(null != member_sync && null != member_sync.get("avatar") && !"".equals(member_sync.get("avatar"))){
						avatar = (String) member_sync.get("avatar");
					}
					
					// 如果今天偷取过十个人了,则其他好友不展示可偷取标识
					if(count == friendNum) {
						if(sneakCount == 0) {
							// 今天偷取过十个人了,这个人没偷取过,则不可偷取了
							stealFlag = "0";
						}
					}
					
					friend.setNickName(nickName);
					friend.setAvatar(avatar);
					friend.setFriendMemberCode(friend_code);
					friend.setStealFlag(stealFlag);
					
					friendList.add(friend);
				}
				
				if(count == friendNum) {
					// 如果今天偷取过十个人了,就不展示随机偷取了
					randomFlag = "0";
					randomMemberCode = "";
				}else {					
					// 如果偷过的好友+可偷的好友 <10 就展示随机偷取
					if(stealFriendNum < friendNum) {
						// 查看是否能找到可以偷取水滴的用户,并随机选择一个账号
						String randomString = getRandomMember(event_code,memberCode);
						String[] split = randomString.split(",");
						randomFlag = split[0];
						if("0".equals(randomFlag)) {
							randomMemberCode = "";
						}else {							
							randomMemberCode = split[1];
						}
					}
				}
				
			}else { // 如果没有好友
				if(count == friendNum) {
					// 如果今天偷取过十个人了,就不展示随机偷取了
					randomFlag = "0";
					randomMemberCode = "";
				}else {					
					// 查看是否能找到可以偷取水滴的用户,并随机选择一个账号
					String randomString = getRandomMember(event_code,memberCode);
					String[] split = randomString.split(",");
					randomFlag = split[0];
					if("0".equals(randomFlag)) {
						randomMemberCode = "";
					}else {							
						randomMemberCode = split[1];
					}
				}
			}
			
		}
		
		result.setFriendList(friendList);
		result.setRandomFlag(randomFlag);
		result.setRandomMemberCode(randomMemberCode);
		
		return result;
	}

	/**
	 * 查看是否能找到可以偷取水滴的用户,并随机选择一个账号
	 * @return
	 */
	private String getRandomMember(String event_code, String memberCode) {
		String randomString = "";
		// 根据水滴表有效水滴查出用户
		String sql = "SELECT DISTINCT(w.member_code) FROM sc_huodong_farm_user_water w WHERE w.event_code = '"+event_code+"' AND w.flag = '1'" + 
				" AND w.water_num > (SELECT c.begin_num FROM sc_huodong_farm_config c WHERE c.type = '449748480014') " +
				" AND w.member_code NOT IN (SELECT friend_code FROM sc_huodong_farm_user_friend WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"')"+
				" ORDER BY RAND() LIMIT 1";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_huodong_farm_user_water").dataSqlList(sql, new MDataMap());
		if(null != dataSqlList && dataSqlList.size() > 0) {
			// 排除好友用户
			List<String> list = new ArrayList<String>();
			for (Map<String, Object> map : dataSqlList) {
				String member_code = (String) map.get("member_code");
				if(!member_code.equals(memberCode)) {					
					String sql1 = "SELECT * FROM sc_huodong_farm_user_friend WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND friend_code = '"+member_code+"'";
					Map<String, Object> dataSqlOne = DbUp.upTable("sc_huodong_farm_user_friend").dataSqlOne(sql1, new MDataMap());
					if(null == dataSqlOne) {
						// 不是好友,加入list
						list.add(member_code);
					}
				}
			}
			if(list.size() > 0) {
				if(list.size() == 1) {
					randomString = "1," + list.get(0);
				}else {					
					// 随机选择一个用户
					int begin_num = 1;
					int end_num = list.size();
					Random random = new Random();
					int ran = random.nextInt(end_num)%(end_num-begin_num+1) + begin_num;
					String randomMemberCode = list.get(ran-1);
					randomString = "1," + randomMemberCode;
				}
			}else {
				randomString = "0,";
			}
		}else {
			// 没有可偷取的用户
			randomString = "0,";
		}
		
		return randomString;
	}
	
}
