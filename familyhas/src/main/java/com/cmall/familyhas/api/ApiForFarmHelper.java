package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmHelperInput;
import com.cmall.familyhas.api.result.ApiForFarmHelperResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取助力信息接口
 * @author lgx
 *
 */
public class ApiForFarmHelper extends RootApiForVersion<ApiForFarmHelperResult, ApiForFarmHelperInput> {

	@Override
	public ApiForFarmHelperResult Process(ApiForFarmHelperInput inputParam, MDataMap mRequestMap) {
		ApiForFarmHelperResult result = new ApiForFarmHelperResult();

		String helpMessage = "";
		String newHelpMessage = "";
		List<String> avatarList = new ArrayList<String>();
		
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
		
		// 查询当前时间段内已经发布状态的惠惠农场活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的惠惠农场活动!");
			return result;
		}else {
			String event_code = (String) eventInfoMap.get("event_code");
			
			// 每人助力获取得雨露
			MDataMap helpWaterMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480011");
			int helpWater = MapUtils.getIntValue(helpWaterMap,"begin_num");
			// 满5人额外获得雨露
			MDataMap helpWaterMap5 = DbUp.upTable("sc_huodong_farm_config").one("type","449748480012");
			int helpWater5 = MapUtils.getIntValue(helpWaterMap5,"begin_num");
			
			helpMessage = "每人助力得"+helpWater+"g、满5人再得"+helpWater5+"g";
			
			// 邀请新用户助力且种树额外获得
			MDataMap newWaterMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480013");
			int newWater = MapUtils.getIntValue(newWaterMap,"begin_num");
			
			newHelpMessage = "邀请新用户助力且种树，额外得"+newWater+"g";
			
			String nowDate = nowTime.substring(0, 10);
			//String userHelpSql = "SELECT * FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND help_time >= '"+nowDate+" 00:00:00"+"' AND help_time <= '"+nowDate+" 23:59:59"+"'  ORDER BY help_time ASC LIMIT 5";
			String userHelpSql = "SELECT * FROM ( " + 
					"	SELECT b.member_code, b.union_id invite_code, b.help_time FROM sc_huodong_farm_user_help_no_login b  " + 
					"	WHERE b.event_code = '"+event_code+"' AND b.member_code = '"+memberCode+"' AND b.help_time >= '"+nowDate+" 00:00:00"+"' AND b.help_time <= '"+nowDate+" 23:59:59"+"' " + 
					"UNION ALL " + 
					"	SELECT a.member_code, a.help_member_code invite_code, a.help_time FROM sc_huodong_farm_user_help a  " + 
					"	WHERE a.event_code = '"+event_code+"' AND a.member_code = '"+memberCode+"' AND a.help_time >= '"+nowDate+" 00:00:00"+"' AND a.help_time <= '"+nowDate+" 23:59:59"+"' " + 
					") c ORDER BY c.help_time ASC LIMIT 5";
			List<Map<String, Object>> userHelpList = DbUp.upTable("sc_huodong_farm_user_help").dataSqlList(userHelpSql, new MDataMap());
			if(null != userHelpList) {
				for (Map<String, Object> map : userHelpList) {
					String help_member_code = (String) map.get("invite_code");
					String avatar = "";
					Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+help_member_code+"'", new MDataMap());
					if(null != member_sync){
						avatar = MapUtils.getString(member_sync, "avatar", "");
					}else {
						MDataMap helpUser = DbUp.upTable("sc_huodong_farm_user_help_no_login").one("union_id",help_member_code);
						if(helpUser != null) {
							avatar = MapUtils.getString(helpUser, "avatar", "");
						}
					}
					avatarList.add(avatar);
				}
			}
		}
		
		result.setHelpMessage(helpMessage);
		result.setNewHelpMessage(newHelpMessage);
		result.setAvatarList(avatarList);
		
		return result;
	}

	
}
