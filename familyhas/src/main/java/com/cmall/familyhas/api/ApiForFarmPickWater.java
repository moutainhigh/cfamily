package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmPickWaterInput;
import com.cmall.familyhas.api.result.ApiForFarmPickWaterResult;
import com.cmall.familyhas.service.FarmService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 摘水滴接口(摘自己的/偷别人的)
 * @author lgx
 *
 */
public class ApiForFarmPickWater extends RootApiForVersion<ApiForFarmPickWaterResult, ApiForFarmPickWaterInput> {

	private FarmService farmService = new FarmService();
	
	@Override
	public ApiForFarmPickWaterResult Process(ApiForFarmPickWaterInput inputParam, MDataMap mRequestMap) {
		ApiForFarmPickWaterResult result = new ApiForFarmPickWaterResult();
		
		String waterCode = inputParam.getWaterCode();
		int kettleWater = 0;
		int waterNum = 0;
		int stealWaterNum = 0;
		
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
			// 查询该水滴是自己的还是别人的
			String waterSql = "SELECT * FROM sc_huodong_farm_user_water WHERE water_code = '"+waterCode+"' AND event_code = '"+event_code+"' AND flag = '1'";
			Map<String, Object> waterMap = DbUp.upTable("sc_huodong_farm_user_water").dataSqlOne(waterSql, new MDataMap());
			if(null != waterMap) {
				String memberCodeWater = (String) waterMap.get("member_code");
				if(memberCodeWater.equals(memberCode)) {
					// 自己的,查主库,看看今天是否摘这个水滴(防止重复摘取)
					String sneakLogSql = "SELECT * FROM sc_huodong_farm_user_sneak_log WHERE stolen_water_code = '"+waterCode+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
					List<Map<String, Object>> sneakLogList = DbUp.upTable("sc_huodong_farm_user_sneak_log").upTemplate().queryForList(sneakLogSql, new HashMap<String, String>());
					if(null != sneakLogList && sneakLogList.size() > 0) { // 偷取过
						// 摘取的水滴为0,可能是多次点击摘取,也可能是恰巧过期,因此只需返回水壶剩余量
						MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
						kettleWater = Integer.parseInt(kettle.get("kettle_water"));
					}else {
						// 自己的,直接摘取
						// 水滴克数
						int water_num = 0;
						// 摘取雨露时为水滴加锁
						String waterLockKey = Constants.WATER_PREFIX + waterCode;
						String waterLockCode = "";
						try {
							long beginTime = System.currentTimeMillis();
							while("".equals(waterLockCode = KvHelper.lockCodes(1, waterLockKey))) {
								if((System.currentTimeMillis() - beginTime) > 3000L) {
									result.setResultCode(0);
									result.setResultMessage("请求超时");
									return result;
								}
								Thread.sleep(1);
							}
							// 查主库
							//MDataMap water = DbUp.upTable("sc_huodong_farm_user_water").one("event_code", event_code, "water_code", waterCode, "flag", "1");
							List<Map<String, Object>> waterList = DbUp.upTable("sc_huodong_farm_user_water").upTemplate().queryForList("SELECT * FROM sc_huodong_farm_user_water WHERE event_code = '"+event_code+"' AND water_code = '"+waterCode+"' AND flag = '1' ", new HashMap<String, String>());
							if(null != waterList && waterList.size() > 0) {
								Map<String, Object> water = waterList.get(0);
								if(water != null) {
									water_num = MapUtils.getIntValue(water, "water_num");
									stealWaterNum = water_num;
									// 水滴量变为0,且置为无效
									DbUp.upTable("sc_huodong_farm_user_water").dataUpdate(new MDataMap("water_code", waterCode, "water_num", "0", "flag", "0"), "water_num,flag","water_code");
									// 收取自己的水滴记录扣减日志
									MDataMap stealLogMap = new MDataMap();
									stealLogMap.put("event_code", event_code);
									stealLogMap.put("member_code", memberCode);
									stealLogMap.put("stolen_code", memberCode);
									stealLogMap.put("stolen_water_code", waterCode);
									stealLogMap.put("create_time", nowTime);
									DbUp.upTable("sc_huodong_farm_user_sneak_log").dataInsert(stealLogMap);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							result.setResultCode(0);
							result.setResultMessage("系统异常");
							return result;
						} finally {
							if(!"".equals(waterLockCode)) KvHelper.unLockCodes(waterLockCode, waterLockKey);
						}
						
						if(water_num > 0) {
							// 添加雨露时为水壶加锁
							MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
							String kettleCode = kettle.get("kettle_code");
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
								int newKettleWater = water_num + Integer.parseInt(kettle.get("kettle_water"));
								//DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
								// 更新水壶水量时用数据库水壶量 + 摘取的水滴量
								DbUp.upTable("sc_huodong_farm_user_kettle").dataExec("UPDATE sc_huodong_farm_user_kettle SET kettle_water = kettle_water + "+water_num+" WHERE kettle_code = '"+kettleCode+"'", new MDataMap());
								kettleWater = newKettleWater;
								// 记录签到送雨露日志
								MDataMap logMap = new MDataMap();
								logMap.put("event_code", event_code);
								logMap.put("member_code", memberCode);
								logMap.put("description", "收取雨露获得");
								logMap.put("create_time", nowTime);
								logMap.put("water_num", "+"+water_num);
								DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap);
							} catch (Exception e) {
								e.printStackTrace();
								result.setResultCode(0);
								result.setResultMessage("系统异常");
								return result;
							} finally {
								if(!"".equals(kettleLockCode)) KvHelper.unLockCodes(kettleLockCode, kettleLockKey);
							}
						}else {
							// 摘取的水滴为0,可能是多次点击摘取,也可能是恰巧过期,因此只需返回水壶剩余量
							MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
							kettleWater = Integer.parseInt(kettle.get("kettle_water"));
						}
					}
					
				}else {
					// 别人的,查主库,先看今天是否偷过这个水滴
					String sneakLogSql = "SELECT * FROM sc_huodong_farm_user_sneak_log WHERE stolen_water_code = '"+waterCode+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
					List<Map<String, Object>> sneakLogList = DbUp.upTable("sc_huodong_farm_user_sneak_log").upTemplate().queryForList(sneakLogSql, new HashMap<String, String>());
					if(null != sneakLogList && sneakLogList.size() > 0) { // 偷取过
						result.setResultCode(-1);
						result.setResultMessage("今天已经偷过该水滴");
						return result;
					}
					// 偷取水滴克数
					int stealWater = 0;
					// 摘取雨露时为水滴加锁
					String waterLockKey = Constants.WATER_PREFIX + waterCode;
					String waterLockCode = "";
					try {
						long beginTime = System.currentTimeMillis();
						while("".equals(waterLockCode = KvHelper.lockCodes(1, waterLockKey))) {
							if((System.currentTimeMillis() - beginTime) > 3000L) {
								result.setResultCode(0);
								result.setResultMessage("请求超时");
								return result;
							}
							Thread.sleep(1);
						}
						// 查主库
						//MDataMap water = DbUp.upTable("sc_huodong_farm_user_water").one("event_code", event_code, "water_code", waterCode, "flag", "1");
						List<Map<String, Object>> waterList = DbUp.upTable("sc_huodong_farm_user_water").upTemplate().queryForList("SELECT * FROM sc_huodong_farm_user_water WHERE event_code = '"+event_code+"' AND water_code = '"+waterCode+"' AND flag = '1' ", new HashMap<String, String>());
						if(null != waterList && waterList.size() > 0) {
							Map<String, Object> water = waterList.get(0);
							if(water != null) {
								int water_num = MapUtils.getIntValue(water,("water_num"));
								// 剩余水滴
								int newWater = 0;
								// 水滴保底克数
								MDataMap waterEndMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480014");
								int endWater = MapUtils.getIntValue(waterEndMap,"begin_num");
								if(water_num <= endWater) {
									result.setResultCode(-1);
									result.setResultMessage("系统异常");
									return result;
								}else {
									// 保底3克,随机偷取1-10克,不足的以最大偷取值为准
									// 偷取水滴随机范围
									MDataMap randomWaterMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480009");
									int begin_num = MapUtils.getIntValue(randomWaterMap,"begin_num");
									int end_num = MapUtils.getIntValue(randomWaterMap,"end_num");
									if((water_num - endWater) < end_num) {
										end_num = water_num - endWater;
									}
									Random random = new Random();
									stealWater = random.nextInt(end_num)%(end_num-begin_num+1) + begin_num;
								}
								// 偷取水滴量
								stealWaterNum = stealWater;
								newWater = water_num - stealWater;
								// 水滴剩余量
								waterNum = newWater;
								DbUp.upTable("sc_huodong_farm_user_water").dataUpdate(new MDataMap("water_code", waterCode, "water_num", newWater+""), "water_num","water_code");
								// 收取别人的水滴记录扣减日志
								// 偷取人昵称
								String nickName = "";
								Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"'", new MDataMap());
								if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
									// 如果昵称是空,查询手机号
									Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"'", new MDataMap());
									nickName = (String) login_info.get("login_name");
									if(farmService.isPhone(nickName)) {
										nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
									}
								}else { // 如果昵称不是空
									nickName = (String) member_sync.get("nickname");
								}
								MDataMap logMap = new MDataMap();
								logMap.put("event_code", event_code);
								logMap.put("member_code", memberCodeWater);
								logMap.put("description", "被"+nickName+"偷取");
								logMap.put("other_member_code", memberCode);
								logMap.put("other_nickname", nickName);
								logMap.put("create_time", nowTime);
								logMap.put("water_num", "-"+stealWater);
								DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap);
								
								// 收取别人的水滴记录偷取日志
								MDataMap stealLogMap = new MDataMap();
								stealLogMap.put("event_code", event_code);
								stealLogMap.put("member_code", memberCode);
								stealLogMap.put("stolen_code", memberCodeWater);
								stealLogMap.put("stolen_water_code", waterCode);
								stealLogMap.put("create_time", nowTime);
								DbUp.upTable("sc_huodong_farm_user_sneak_log").dataInsert(stealLogMap);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						result.setResultCode(0);
						result.setResultMessage("系统异常");
						return result;
					} finally {
						if(!"".equals(waterLockCode)) KvHelper.unLockCodes(waterLockCode, waterLockKey);
					}
					
					if(stealWater > 0) {
						// 添加雨露时为水壶加锁
						MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
						String kettleCode = kettle.get("kettle_code");
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
							int newKettleWater = stealWater + Integer.parseInt(kettle.get("kettle_water"));
							//DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
							// 更新水壶水量时用数据库水壶量 + 摘取的水滴量
							DbUp.upTable("sc_huodong_farm_user_kettle").dataExec("UPDATE sc_huodong_farm_user_kettle SET kettle_water = kettle_water + "+stealWater+" WHERE kettle_code = '"+kettleCode+"'", new MDataMap());
							kettleWater = newKettleWater;
							// 记录签到送雨露日志
							// 被偷取人昵称
							String nickName2 = "";
							Map<String, Object> member_sync2 = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCodeWater+"'", new MDataMap());
							if(null == member_sync2.get("nickname") || "".equals(member_sync2.get("nickname"))){
								// 如果昵称是空,查询手机号
								Map<String, Object> login_info2 = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCodeWater+"'", new MDataMap());
								nickName2 = (String) login_info2.get("login_name");
								if(farmService.isPhone(nickName2)) {
									nickName2 = nickName2.substring(0, 3) + "****" + nickName2.substring(7);
								}
							}else { // 如果昵称不是空
								nickName2 = (String) member_sync2.get("nickname");
							}
							MDataMap logMap2 = new MDataMap();
							logMap2.put("event_code", event_code);
							logMap2.put("member_code", memberCode);
							logMap2.put("description", "偷取"+nickName2);
							logMap2.put("other_member_code", memberCodeWater);
							logMap2.put("other_nickname", nickName2);
							logMap2.put("create_time", nowTime);
							logMap2.put("water_num", "+"+stealWater);
							DbUp.upTable("sc_huodong_farm_log").dataInsert(logMap2);
						} catch (Exception e) {
							e.printStackTrace();
							result.setResultCode(0);
							result.setResultMessage("系统异常");
							return result;
						} finally {
							if(!"".equals(kettleLockCode)) KvHelper.unLockCodes(kettleLockCode, kettleLockKey);
						}
					}else {
						// 偷取的水滴为0,可能是水滴已经被摘取,也可能是恰巧过期,因此只需返回水壶剩余量
						MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
						kettleWater = Integer.parseInt(kettle.get("kettle_water"));
					}
				}
			}else {
				// 没查到水滴
				MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCode);
				kettleWater = Integer.parseInt(kettle.get("kettle_water"));
			}
		}
		
		result.setKettleWater(kettleWater);
		result.setWaterCode(waterCode);
		result.setWaterNum(waterNum);
		result.setStealWaterNum(stealWaterNum);
		
		return result;
	}

	
}
