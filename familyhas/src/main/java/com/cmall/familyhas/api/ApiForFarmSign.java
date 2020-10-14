package com.cmall.familyhas.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForFarmSignInput;
import com.cmall.familyhas.api.model.FarmSign;
import com.cmall.familyhas.api.result.ApiForFarmSignResult;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 农场签到接口
 * @author lgx
 *
 */
public class ApiForFarmSign extends RootApiForVersion<ApiForFarmSignResult, ApiForFarmSignInput> {

	@Override
	public ApiForFarmSignResult Process(ApiForFarmSignInput inputParam, MDataMap mRequestMap) {
		ApiForFarmSignResult result = new ApiForFarmSignResult();
		
		String signPrompt = "";
		String signFlag = "0";
		int signNum = 0;
		List<FarmSign> farmSignList = new ArrayList<FarmSign>();
		
		String linkType = inputParam.getLinkType();
		String signCode = inputParam.getSignCode();
		
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
			
			// 查询签到列表
			String signSql= "SELECT * FROM sc_huodong_event_farm_sign WHERE event_code = '"+event_code+"' ORDER BY sign_num ASC";
			List<Map<String, Object>> signList = DbUp.upTable("sc_huodong_event_farm_sign").dataSqlList(signSql, new MDataMap());
			if(null != signList && signList.size() > 0) {
				for (Map<String, Object> map : signList) {
					FarmSign farmSign = new FarmSign();
					String sign_code = (String) map.get("sign_code");
					String sign_day = (String) map.get("sign_day");
					int sign_water = (int) map.get("sign_water");
					farmSign.setSignCode(sign_code);
					farmSign.setSignDay(sign_day);
					farmSign.setSignWater(sign_water);
					
					farmSignList.add(farmSign);
				}
			}
			
			if("1".equals(linkType)) {
				// 进入签到列表
				// 今日是否签到
				String userSignSql= "SELECT * FROM sc_huodong_farm_user_sign WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
				Map<String, Object> userSign = DbUp.upTable("sc_huodong_farm_user_sign").dataSqlOne(userSignSql, new MDataMap());
				// 最后签到信息
				String daySql = "SELECT * FROM sc_huodong_farm_user_sign WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' ORDER BY create_time DESC";
				Map<String, Object> dayMap = DbUp.upTable("sc_huodong_farm_user_sign").dataSqlOne(daySql, new MDataMap());
				if(null != userSign) {
					// 已签到
					signFlag = "0";
					// 已签到,查询今天签到是第几天
					String sign_code = (String) userSign.get("sign_code");
					MDataMap todaySign = DbUp.upTable("sc_huodong_event_farm_sign").one("sign_code",sign_code);
					// 签到天数
					signNum = Integer.parseInt(todaySign.get("sign_num"));
					// 提示语
					signPrompt = "已连续签到"+signNum+"天，继续加油啊~";
				}else {
					// 未签到
					signFlag = "1";
					// 未签到,查询最后签到时间
					if(null != dayMap) {
						String create_time = (String) dayMap.get("create_time");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DATE, - 1);           
						Date time = c.getTime();         
						String yesterday = sdf.format(time);
						if(yesterday.equals(create_time.substring(0, 10))) {
							// 如果最后一天签到时间是昨天
							MDataMap yesterdaySign = DbUp.upTable("sc_huodong_event_farm_sign").one("sign_code",dayMap.get("sign_code").toString());
							int  yesterdaySignNum = Integer.parseInt(yesterdaySign.get("sign_num"));
							if(yesterdaySignNum < 7) {								
								// 签到天数
								signNum = Integer.parseInt(yesterdaySign.get("sign_num"));
								// 提示语
								MDataMap todaySign = DbUp.upTable("sc_huodong_event_farm_sign").one("event_code",event_code,"sign_num",signNum+1+"");
								signPrompt = todaySign.get("sign_day");
							}else {
								// 昨天是第七天签到,今天从头开始签到
								signNum = 0;
							}
						}else {
							// 如果最后一天签到时间不是昨天,签到中断,从头开始签到
							signNum = 0;
						}
					}else {
						// 从未签到过
						signNum = 0;
					}
				}
				if(signNum == 0) {
					MDataMap todaySign = DbUp.upTable("sc_huodong_event_farm_sign").one("event_code",event_code,"sign_num","1");
					signPrompt = todaySign.get("sign_day");
				}
			}else if("2".equals(linkType)) {
				// 点击签到
				// 今日是否签到
				String userSignSql= "SELECT * FROM sc_huodong_farm_user_sign WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
				List<Map<String, Object>> userSignList = DbUp.upTable("sc_huodong_farm_user_kettle").upTemplate().queryForList(userSignSql, new HashMap<String, String>());
				if(null != userSignList && userSignList.size() > 0) {
					result.setResultCode(-1);
					result.setResultMessage("您今日已经签到");
					return result;
				}else {
					MDataMap userSignMap = new MDataMap();
					userSignMap.put("create_time", nowTime);
					userSignMap.put("sign_code", signCode);
					userSignMap.put("member_code", memberCode);
					userSignMap.put("event_code", event_code);
					DbUp.upTable("sc_huodong_farm_user_sign").dataInsert(userSignMap);
					
					// 已签到
					signFlag = "0";
					// 签到天数
					MDataMap todaySign = DbUp.upTable("sc_huodong_event_farm_sign").one("event_code",event_code,"sign_code",signCode);
					signNum = Integer.parseInt(todaySign.get("sign_num"));
					signPrompt = "已连续签到"+signNum+"天，继续加油啊~";
					
					// 为水壶添加雨露
					// 添加雨露克数
					int sign_water = Integer.parseInt(todaySign.get("sign_water"));
					MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
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
						kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
						int newKettleWater = sign_water + Integer.parseInt(kettle.get("kettle_water"));
						DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
						
						// 记录签到送雨露日志
						MDataMap logMap = new MDataMap();
						logMap.put("event_code", event_code);
						logMap.put("member_code", memberCode);
						logMap.put("description", "签到获得");
						logMap.put("create_time", nowTime);
						logMap.put("water_num", "+"+sign_water);
						DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap);
					} catch (Exception e) {
						e.printStackTrace();
						result.setResultCode(0);
						result.setResultMessage("系统异常");
						return result;
					} finally {
						if(!"".equals(kettleLockCode)) KvHelper.unLockCodes(kettleLockCode, kettleLockKey);
					}
				}
			}
		}
		
		result.setFarmSignList(farmSignList);
		result.setSignFlag(signFlag);
		result.setSignNum(signNum);
		result.setSignPrompt(signPrompt);
		
		return result;
	}

	
}
