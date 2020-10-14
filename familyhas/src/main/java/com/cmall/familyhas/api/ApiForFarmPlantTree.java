package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmPlantTreeInput;
import com.cmall.familyhas.api.result.ApiForFarmPlantTreeResult;
import com.cmall.familyhas.service.FarmService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 农场种树接口
 * @author lgx
 *
 */
public class ApiForFarmPlantTree extends RootApiForVersion<ApiForFarmPlantTreeResult, ApiForFarmPlantTreeInput> {

	private FarmService farmService = new FarmService();
	
	@Override
	public ApiForFarmPlantTreeResult Process(ApiForFarmPlantTreeInput inputParam, MDataMap mRequestMap) {
		ApiForFarmPlantTreeResult result = new ApiForFarmPlantTreeResult();
		
		// 种植的果树类型
		String treeType = inputParam.getTreeType();
		
		if(null == treeType || "".equals(treeType)) {
			result.setResultCode(-1);
			result.setResultMessage("请选择要种的果树!");
			return result;
		}
		
		String plantFlag = "0";
		
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
			// 查询是否有没成熟的树,避免存在多棵成长中的树
			String userTreeSql = "SELECT * FROM sc_huodong_farm_user_tree WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"' AND change_flag = '0'";
			//Map<String, Object> userTreeMap = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(userTreeSql, new MDataMap());
			List<Map<String, Object>> queryForList = DbUp.upTable("sc_huodong_farm_user_tree").upTemplate().queryForList(userTreeSql, new HashMap<String, String>());
			if(null == queryForList || queryForList.size() == 0) {
				// 如果是空,说明暂无树苗,需要种树
				// 查询是否有水壶
				List<Map<String, Object>> kettleList = DbUp.upTable("sc_huodong_farm_user_kettle").upTemplate()
						.queryForList("SELECT * FROM sc_huodong_farm_user_kettle WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"'", new HashMap<String, String>());
				String kettle_code = "";
				if(null == kettleList || kettleList.size() == 0) {
					// 如果为空就是新用户,创建一条记录
					kettle_code = WebHelper.upCode("FKE");
					MDataMap userKettleMap = new MDataMap();
					userKettleMap.put("kettle_code", kettle_code);
					userKettleMap.put("kettle_water", "0");
					userKettleMap.put("member_code", memberCode);
					userKettleMap.put("event_code", event_code);
					DbUp.upTable("sc_huodong_farm_user_kettle").dataInsert(userKettleMap);
				}else {
					kettle_code = MapUtils.getString(kettleList.get(0), "kettle_code");
				}
				
				// 种树
				// 果树编码
				String tree_code = WebHelper.upCode("GS");
				MDataMap addTreeMap = new MDataMap();
				addTreeMap.put("event_code", event_code);
				addTreeMap.put("member_code", memberCode);
				addTreeMap.put("tree_code", tree_code);
				addTreeMap.put("tree_type", treeType);
				// 新种的树阶段为 幼苗
				addTreeMap.put("tree_stage", "449748450001");
				// 剩余贡献度初始化100%
				addTreeMap.put("surplus_contribute", "100");
				addTreeMap.put("change_flag", "0");
				addTreeMap.put("create_time", nowTime);
				DbUp.upTable("sc_huodong_farm_user_tree").dataInsert(addTreeMap);
				
				plantFlag = "1";
				
				// 种树成功,看是不是首次种树,首次种树送20g雨露
				String firstTreeSql = "SELECT count(1) num FROM sc_huodong_farm_user_tree WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"' AND change_flag = '1'";
				//List<Map<String, Object>> firstTreeList = DbUp.upTable("sc_huodong_farm_user_tree").upTemplate().queryForList(firstTreeSql, new HashMap<String, String>());
				Map<String, Object> treeMap = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(firstTreeSql, new MDataMap());
				if(treeMap == null || MapUtils.getIntValue(treeMap, "num") == 0) {
					// 添加雨露时为水壶加锁
					String kettleLockKey = Constants.KETTLE_PREFIX + kettle_code;
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
						// 首次种树送20g雨露
						MDataMap firstTreeMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480016");
						int firstTreeWater = MapUtils.getIntValue(firstTreeMap,"begin_num");
						
						DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettle_code, "kettle_water", firstTreeWater + ""), "kettle_water","kettle_code");
						
						// 记录首次种树送雨露日志
						MDataMap logMap = new MDataMap();
						logMap.put("event_code", event_code);
						logMap.put("member_code", memberCode);
						logMap.put("description", "首次种树获得");
						logMap.put("create_time", nowTime);
						logMap.put("water_num", "+"+firstTreeWater);
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
				
				// 种树成功,看是不是新用户
				Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"'", new MDataMap());
				if(null != login_info) {
					String create_time = (String) login_info.get("create_time");
					if(nowTime.substring(0, 10).equals(create_time.substring(0, 10))) {
						// 如果是今天注册的新用户,并且种树,查看是否被人邀请
						String begin = nowTime.substring(0, 10)+" 00:00:00";
						String end = nowTime.substring(0, 10)+" 23:59:59";
						String helpSql = "SELECT * FROM sc_huodong_farm_user_help WHERE event_code = '"+event_code+"' AND help_member_code = '"+memberCode+"' AND help_time >= '"+begin+"' AND help_time <= '"+end+"'";
						Map<String, Object> helpMap = DbUp.upTable("sc_huodong_farm_user_help").dataSqlOne(helpSql, new MDataMap());
						if(null != helpMap) {
							// 为好友助力过,满足邀请新用户助力且种树，好友额外得50g
							String memberCodeYqr = (String) helpMap.get("member_code");
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
								// 邀请新用户助力且种树额外获得
								MDataMap newWaterMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480013");
								int newWater = MapUtils.getIntValue(newWaterMap,"begin_num");
								
								kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", event_code, "member_code", memberCodeYqr);
								int newKettleWater = newWater + Integer.parseInt(kettle.get("kettle_water"));
								DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
								
								// 记录邀请新用户助力且种树额外获得雨露日志
								// 助力人昵称
								String nickName = "";
								Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"'", new MDataMap());
								if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
									// 如果昵称是空,查询手机号
									Map<String, Object> login_info2 = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"'", new MDataMap());
									nickName = (String) login_info2.get("login_name");
									if(farmService.isPhone(nickName)) {
										nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
									}
								}else { // 如果昵称不是空
									nickName = (String) member_sync.get("nickname");
								}
								MDataMap logMap = new MDataMap();
								logMap.put("event_code", event_code);
								logMap.put("member_code", memberCodeYqr);
								logMap.put("description", nickName+"新用户为你助力且种树额外获得");
								logMap.put("create_time", nowTime);
								logMap.put("water_num", "+"+newWater);
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
			}else {
				result.setPlantFlag(plantFlag);
				result.setResultCode(-1);
				result.setResultMessage("您已经有正在成长的果树!");
				return result;
			}
		}
		
		result.setPlantFlag(plantFlag);
		
		return result;
	}

	
}
