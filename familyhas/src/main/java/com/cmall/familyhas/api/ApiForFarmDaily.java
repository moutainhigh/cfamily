package com.cmall.familyhas.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForFarmDailyInput;
import com.cmall.familyhas.api.model.FarmDaily;
import com.cmall.familyhas.api.result.ApiForFarmDailyResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取农场动态接口(最近七天的动态日志)
 * @author lgx
 *
 */
public class ApiForFarmDaily extends RootApiForVersion<ApiForFarmDailyResult, ApiForFarmDailyInput> {

	@Override
	public ApiForFarmDailyResult Process(ApiForFarmDailyInput inputParam, MDataMap mRequestMap) {
		ApiForFarmDailyResult result = new ApiForFarmDailyResult();

		List<FarmDaily> dailyList = new ArrayList<FarmDaily>();
		
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
			
			// 查询最近7天的动态数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, - 6);           
		    Date time = c.getTime();         
		    String beginDay = sdf.format(time);
			
		    String farmLogSql = "SELECT * FROM sc_huodong_farm_log WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND create_time >= '"+beginDay+" 00:00:00"+"' AND create_time <= '"+nowTime+"' ORDER BY create_time DESC, zid DESC ";
			List<Map<String, Object>> farmLogList = DbUp.upTable("sc_huodong_farm_log").dataSqlList(farmLogSql, new MDataMap());
			if(null != farmLogList && farmLogList.size() > 0) {
				for (Map<String, Object> map : farmLogList) {
					FarmDaily farmDaily = new FarmDaily();
					String other_member_code = (String) map.get("other_member_code");
					String other_nickname = (String) map.get("other_nickname");
					String water_num = (String) map.get("water_num"); // +10g / -10g
					String create_time = (String) map.get("create_time");
					String description = (String) map.get("description");
					
					farmDaily.setCreateTime(create_time.substring(0, 10));
					farmDaily.setDescription(description);
					farmDaily.setOtherMemberCode(other_member_code);
					farmDaily.setOtherNickname(other_nickname);
					farmDaily.setWaterNum(water_num);
					
					dailyList.add(farmDaily);
				}
			}
		}
		
		result.setDailyList(dailyList);
		
		return result;
	}


	
}
