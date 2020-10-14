package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmInviteSuccessInput;
import com.cmall.familyhas.api.result.ApiForFarmInviteSuccessResult;
import com.cmall.familyhas.service.FarmService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 助力/邀请成功接口
 * @author lgx
 *
 */
public class ApiForFarmInviteSuccess extends RootApiForVersion<ApiForFarmInviteSuccessResult, ApiForFarmInviteSuccessInput> {

	private FarmService farmService = new FarmService();
	
	@Override
	public ApiForFarmInviteSuccessResult Process(ApiForFarmInviteSuccessInput inputParam, MDataMap mRequestMap) {
		ApiForFarmInviteSuccessResult result = new ApiForFarmInviteSuccessResult();
		
		String memberCodeYqr = inputParam.getMemberCodeYqr();
		// 验证传入的邀请人编号是否正确
		Map<String, Object> login_info_yqr = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCodeYqr+"'", new MDataMap());
		if(login_info_yqr == null) {
			result.setResultCode(-1);
			result.setResultMessage("邀请人信息有误!");
			return result;
		}
		
		// 如果登录了,获取登录人memberCode
		String memberCode = "";
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
		}else {
			result.setResultCode(-1);
			result.setResultMessage("请您先登录!");
			return result;
		}
		
		// 如果被邀请人和邀请人是同一人
		if(memberCode.equals(memberCodeYqr)) {
			result.setResultCode(-1);
			result.setResultMessage("对不起,不能为自己助力");
			return result;
		}
		
		String nowTime = FormatHelper.upDateTime();
		
		// 查询当前时间段内已经发布状态的惠惠农场活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的惠惠农场活动!");
			return result;
		}else {
			String event_code = (String) eventInfoMap.get("event_code");
			// 看两个人是不是好友,如果不是,就创建好友关系
			String userFriend1 = "SELECT * FROM sc_huodong_farm_user_friend WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND friend_code = '"+memberCodeYqr+"'";
			Map<String, Object> userFriendMap1 = DbUp.upTable("sc_huodong_farm_user_friend").dataSqlOne(userFriend1, new MDataMap());
			if(null == userFriendMap1){
				// 创建好友
				MDataMap friendMap = new MDataMap();
				friendMap.put("event_code", event_code);
				friendMap.put("member_code", memberCode);
				friendMap.put("friend_code", memberCodeYqr);
				friendMap.put("create_time", nowTime);
				DbUp.upTable("sc_huodong_farm_user_friend").dataInsert(friendMap);
			}
			String userFriend2 = "SELECT * FROM sc_huodong_farm_user_friend WHERE event_code = '"+event_code+"' AND member_code = '"+memberCodeYqr+"' AND friend_code = '"+memberCode+"'";
			Map<String, Object> userFriendMap2 = DbUp.upTable("sc_hudong_event_info").dataSqlOne(userFriend2, new MDataMap());
			if(null == userFriendMap2){
				// 创建好友
				MDataMap friendMap = new MDataMap();
				friendMap.put("event_code", event_code);
				friendMap.put("member_code", memberCodeYqr);
				friendMap.put("friend_code", memberCode);
				friendMap.put("create_time", nowTime);
				DbUp.upTable("sc_huodong_farm_user_friend").dataInsert(friendMap);
			}
			
			String begin = nowTime.substring(0, 10)+" 00:00:00";
			String end = nowTime.substring(0, 10)+" 23:59:59";
			
			// 今天有没有为该好友助力
			String helpSql = "SELECT * FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND member_code = '"+memberCodeYqr+"' AND help_member_code = '"+memberCode+"' AND help_time >= '"+begin+"' AND help_time <= '"+end+"'";
			Map<String, Object> helpMap = DbUp.upTable("sc_huodong_farm_user_help").dataSqlOne(helpSql, new MDataMap());
			if(null != helpMap) {
				result.setResultCode(-1);
				result.setResultMessage("已经为好友助力");
				return result;
			}
			
			// 今天有多少人为该好友助力过,够5人则不能再助力
			//String helpCountSqlY = "SELECT count(1) num FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND member_code = '"+memberCodeYqr+"' AND help_time >= '"+begin+"' AND help_time <= '"+end+"'";
			String helpCountSqlY = "SELECT count(1) num FROM ( " + 
					"	SELECT b.member_code, b.union_id invite_code, b.help_time FROM sc_huodong_farm_user_help_no_login b  " + 
					"	WHERE b.event_code = '"+event_code+"' AND b.member_code = '"+memberCodeYqr+"' AND b.help_time >= '"+begin+"' AND b.help_time <= '"+end+"' " + 
					"UNION ALL " + 
					"	SELECT a.member_code, a.help_member_code invite_code, a.help_time FROM sc_huodong_farm_user_help a  " + 
					"	WHERE a.event_code = '"+event_code+"' AND a.member_code = '"+memberCodeYqr+"' AND a.help_time >= '"+begin+"' AND a.help_time <= '"+end+"' " + 
					") c ";
			List<Map<String, Object>> helpCountY = DbUp.upTable("sc_huodong_farm_user_help").upTemplate().queryForList(helpCountSqlY, new HashMap<String, String>());
			if(null != helpCountY && helpCountY.size() > 0) {
				if(MapUtils.getIntValue(helpCountY.get(0),"num") >= 5) {
					result.setResultCode(-1);
					result.setResultMessage("好友被助力次数已达上限!");
					return result;
				}
			}
			
			// 今天为几个人助力过,先设置无限大,后期可能限制每人每天只能给有限个好友助力
			// 配置的每人每天可以给多少个好友助力
			MDataMap totalHelpMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480010");
			int totalHelpNum = MapUtils.getIntValue(totalHelpMap,"begin_num");
			
			// 今天为多少人助力过
			String helpCountSql = "SELECT count(1) num FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND help_member_code = '"+memberCode+"' AND help_time >= '"+begin+"' AND help_time <= '"+end+"'";
			List<Map<String, Object>> helpCount = DbUp.upTable("sc_huodong_farm_user_help").upTemplate().queryForList(helpCountSql, new HashMap<String, String>());
			if(null != helpCount && helpCount.size() > 0) {
				int helpNum = MapUtils.getIntValue(helpCount.get(0),"num");
				if(totalHelpNum > helpNum) {
					
				}else {
					result.setResultCode(-1);
					result.setResultMessage("对不起,您今日为好友助力次数达到上限!");
					return result;
				}
			}
			
			// 可以助力
			MDataMap helpSuccessMap = new MDataMap();
			helpSuccessMap.put("event_code", event_code);
			helpSuccessMap.put("member_code", memberCodeYqr);
			helpSuccessMap.put("help_member_code", memberCode);
			helpSuccessMap.put("help_time", nowTime);
			DbUp.upTable("sc_huodong_farm_user_help").dataInsert(helpSuccessMap);
			
			// 助力成功,为好友水壶添加雨露
			MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCodeYqr);
			String kettleCode = kettle.get("kettle_code");
			// 添加雨露时为水壶加锁
			String kettleLockKey = Constants.KETTLE_PREFIX + kettleCode;
			String kettleLockCode = "";
			try {
				long beginTime = System.currentTimeMillis();
				while("".equals(kettleLockCode = KvHelper.lockCodes(1, kettleLockKey))) {
					if((System.currentTimeMillis() - beginTime) > 3000L) {
						result.setResultCode(0);
						result.setResultMessage("请求超时");
						return result;
					}
					Thread.sleep(1);
				}
				// 是否是第五个人
				boolean fiveFlag = false;
				// 每人助力获取得雨露
				MDataMap helpWaterMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480011");
				int helpWater = MapUtils.getIntValue(helpWaterMap,"begin_num");
				// 满5人额外获得雨露
				MDataMap helpWaterMap5 = DbUp.upTable("sc_huodong_farm_config").one("type","449748480012");
				int helpWater5 = MapUtils.getIntValue(helpWaterMap5,"begin_num");
				
				// 这是第几个好友助力
				//String helpCountSqlNow = "SELECT count(1) num FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND member_code = '"+memberCodeYqr+"' AND help_time >= '"+begin+"' AND help_time <= '"+end+"'";
				String helpCountSqlNow = "SELECT count(1) num FROM ( " + 
						"	SELECT b.member_code, b.union_id invite_code, b.help_time FROM sc_huodong_farm_user_help_no_login b  " + 
						"	WHERE b.event_code = '"+event_code+"' AND b.member_code = '"+memberCodeYqr+"' AND b.help_time >= '"+begin+"' AND b.help_time <= '"+end+"' " + 
						"UNION ALL " + 
						"	SELECT a.member_code, a.help_member_code invite_code, a.help_time FROM sc_huodong_farm_user_help a  " + 
						"	WHERE a.event_code = '"+event_code+"' AND a.member_code = '"+memberCodeYqr+"' AND a.help_time >= '"+begin+"' AND a.help_time <= '"+end+"' " + 
						") c ";
				List<Map<String, Object>> helpCountNow = DbUp.upTable("sc_huodong_farm_user_help").upTemplate().queryForList(helpCountSqlNow, new HashMap<String, String>());
				if(null != helpCountNow && helpCountNow.size() > 0) {
					int totalHelp = MapUtils.getIntValue(helpCountNow.get(0),"num");
					if(totalHelp == 5) {
						fiveFlag = true;
					}else if(totalHelp > 5) {
						result.setResultCode(-1);
						result.setResultMessage("助力异常");
						return result;
					}
				}
				
				kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCodeYqr);
				int kettle_water = Integer.parseInt(kettle.get("kettle_water"));
				int newKettleWater = 0;
				if(fiveFlag) {
					// 如果是第五个人助力,获得额外雨露
					newKettleWater = helpWater + helpWater5 + kettle_water;
				}else {
					newKettleWater = helpWater + kettle_water;
				}
				DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
				
				// 记录助力送雨露日志
				// 助力人昵称
				String nickName = "";
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"'", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"'", new MDataMap());
					if(login_info != null) {
						nickName = (String) login_info.get("login_name");
						if(farmService.isPhone(nickName)) {
							nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
						}
					}
				}else { // 如果昵称不是空
					nickName = (String) member_sync.get("nickname");
				}
				MDataMap logMap = new MDataMap();
				logMap.put("event_code", event_code);
				logMap.put("member_code", memberCodeYqr);
				logMap.put("description", nickName+"为你助力获得");
				logMap.put("create_time", nowTime);
				logMap.put("water_num", "+"+helpWater);
				DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap);
				
				if(fiveFlag) {
					// 记录第五个人助力,获得额外雨露日志
					MDataMap logMap5 = new MDataMap();
					logMap5.put("event_code", event_code);
					logMap5.put("member_code", memberCodeYqr);
					logMap5.put("description", "助力满5人额外获得");
					logMap5.put("create_time", nowTime);
					logMap5.put("water_num", "+"+helpWater5);
					DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap5);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setResultCode(0);
				result.setResultMessage("系统异常");
				return result;
			} finally {
				if(!"".equals(kettleLockCode)) KvHelper.unLockCodes(kettleLockCode, kettleLockKey);
			}
		}
		
		return result;
	}

	
	
}
