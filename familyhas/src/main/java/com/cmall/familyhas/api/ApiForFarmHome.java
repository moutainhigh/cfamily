package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForFarmHomeInput;
import com.cmall.familyhas.api.model.FarmEvent;
import com.cmall.familyhas.api.model.FarmTree;
import com.cmall.familyhas.api.model.FarmWater;
import com.cmall.familyhas.api.result.ApiForFarmHomeResult;
import com.cmall.familyhas.service.FarmService;
import com.cmall.familyhas.service.PageActiveService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 农场首页接口
 * @author lgx
 *
 */
public class ApiForFarmHome extends RootApiForVersion<ApiForFarmHomeResult, ApiForFarmHomeInput> {

	private FarmService farmService = new FarmService();

	@Override
	public ApiForFarmHomeResult Process(ApiForFarmHomeInput inputParam, MDataMap mRequestMap) {
		ApiForFarmHomeResult result = new ApiForFarmHomeResult();
		
		// 别人的memberCode
		String othersMemberCode = inputParam.getOthersMemberCode();
		// 验证传入的用户编号是否正确
		if(!"".equals(othersMemberCode)) {
			Map<String, Object> login_info_yqr = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+othersMemberCode+"'", new MDataMap());
			if(login_info_yqr == null) {
				result.setResultCode(-1);
				result.setResultMessage("用户信息有误!");
				return result;
			}
		}
		
		String kettleCode = "";
		int kettleWater = 0;
		String othersNickName = "";
		List<FarmWater> farmWaterlist = new ArrayList<FarmWater>();
		FarmTree farmTree = new FarmTree();
		FarmEvent farmEvent = new FarmEvent();
		
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
			// 活动信息
			String event_code = (String) eventInfoMap.get("event_code");
			farmEvent.setEventCode(event_code);
			
			// 水壶剩余总量
			List<Map<String, Object>> kettleList = DbUp.upTable("sc_huodong_farm_user_kettle").upTemplate()
					.queryForList("SELECT * FROM sc_huodong_farm_user_kettle WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"'", new HashMap<String, String>());
			if(null != kettleList && kettleList.size() > 0) {
				kettleCode = (String) kettleList.get(0).get("kettle_code");
				kettleWater = (int) kettleList.get(0).get("kettle_water");
			}else {
				// 如果为空就是新用户,创建一条记录
				String kettle_code = WebHelper.upCode("FKE");
				MDataMap userKettleMap = new MDataMap();
				userKettleMap.put("kettle_code", kettle_code);
				userKettleMap.put("kettle_water", "0");
				userKettleMap.put("member_code", memberCode);
				userKettleMap.put("event_code", event_code);
				DbUp.upTable("sc_huodong_farm_user_kettle").dataInsert(userKettleMap);
				kettleCode = kettle_code;
			}
			
			if(null == othersMemberCode || "".equals(othersMemberCode)) {
				// 如果没有传别人的编号,说明是进入自己的农场
				// 自己的大树信息
				String userTreeSql = "SELECT * FROM sc_huodong_farm_user_tree WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"' AND change_flag = '0'";
				//Map<String, Object> userTreeMap = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(userTreeSql, new MDataMap());
				List<Map<String, Object>> queryForList = DbUp.upTable("sc_huodong_farm_user_tree").upTemplate().queryForList(userTreeSql, new HashMap<String, String>());
				if(null == queryForList || queryForList.size() == 0) {
					// 如果是空,说明暂无树苗,需要种树
					farmTree = null;
				}else {
					Map<String, Object> userTreeMap = queryForList.get(0);
					BigDecimal surplus_contribute = (BigDecimal)userTreeMap.get("surplus_contribute");
					String tree_stage = (String) userTreeMap.get("tree_stage");
					String tree_code = (String) userTreeMap.get("tree_code");
					String tree_type =(String) userTreeMap.get("tree_type");
					// 果树类型
					String treeName = "";
					if("449748440001".equals(tree_type)) {
						treeName = "柠檬";
					}else if("449748440002".equals(tree_type)) {
						treeName = "核桃";
					}else if("449748440003".equals(tree_type)) {
						treeName = "红枣";
					}else if("449748440004".equals(tree_type)) {
						treeName = "大米";
					}
					String treePrompt = "";
					if("449748450001".equals(tree_stage)) {
						treePrompt = "再浇"+surplus_contribute+"%的雨露"+treeName+"就长大啦";
					}else if("449748450002".equals(tree_stage)) {
						treePrompt = "再浇"+surplus_contribute+"%的雨露"+treeName+"就开花啦";
					}else if("449748450003".equals(tree_stage)) {
						treePrompt = "再浇"+surplus_contribute+"%的雨露"+treeName+"就结果啦";
					}else if("449748450004".equals(tree_stage)) {
						treePrompt = "再浇"+surplus_contribute+"%的雨露"+treeName+"包邮到家";
					}else if("449748450005".equals(tree_stage)) {
						treePrompt = treeName+"成熟啦";
					}
					farmTree.setTreePrompt(treePrompt);
					farmTree.setSurplusContribute(surplus_contribute);
					farmTree.setTreeCode(tree_code);
					farmTree.setTreeStage(tree_stage);
					farmTree.setTreeType(tree_type);
				}
				
				// 自己的水滴信息
				String userWaterSql = "SELECT * FROM sc_huodong_farm_user_water WHERE member_code = '"+memberCode+"' AND event_code = '"+event_code+"' AND flag = '1'";
				List<Map<String, Object>> userWaterList = DbUp.upTable("sc_huodong_farm_user_water").dataSqlList(userWaterSql, new MDataMap());
				if(null != userWaterList && userWaterList.size() > 0) {
					for (Map<String, Object> map : userWaterList) {
						FarmWater farmWater = new FarmWater();
						String water_code = (String) map.get("water_code");
						int water_num = (int) map.get("water_num");
						// 自己的水滴都能摘
						String stealFlag = "1";
						farmWater.setStealFlag(stealFlag);
						farmWater.setWateCode(water_code);
						farmWater.setWaterNum(water_num);
						
						farmWaterlist.add(farmWater);
					}
				}

			}else {
				// 如果传了别人的编号,说明是进入别人的农场
				// 别人的大树信息
				String userTreeSql = "SELECT * FROM sc_huodong_farm_user_tree WHERE member_code = '"+othersMemberCode+"' AND event_code = '"+event_code+"' AND change_flag = '0'";
				Map<String, Object> userTreeMap = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(userTreeSql, new MDataMap());
				if(null == userTreeMap) {
					// 如果是空,说明暂无树苗,需要种树
					farmTree = null;
				}else {
					BigDecimal surplus_contribute = (BigDecimal)userTreeMap.get("surplus_contribute");
					String tree_stage = (String) userTreeMap.get("tree_stage");
					String tree_code = (String) userTreeMap.get("tree_code");
					String tree_type =(String) userTreeMap.get("tree_type");
					
					farmTree.setSurplusContribute(surplus_contribute);
					farmTree.setTreeCode(tree_code);
					farmTree.setTreeStage(tree_stage);
					farmTree.setTreeType(tree_type);
				}
				
				// 检查今天偷取过几个好友,该好友是否能够偷取
				boolean friendStealFlag = true; // 可以偷
				String countSql = "SELECT count(DISTINCT(stolen_code)) num FROM sc_huodong_farm_user_sneak_log WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND stolen_code != '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
				Map<String, Object> countMap = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(countSql, new MDataMap());
				int count = 0;
				if(null != countMap) {
					count = MapUtils.getIntValue(countMap,"num");
				}
				// 展示随机偷取的限制好友数
				MDataMap friendNumMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480015");
				int friendNum = MapUtils.getIntValue(friendNumMap,"begin_num");
				if(count == friendNum) {
					// 如果今天偷取过十个人了,看今天有没有偷过该用户,如果没偷过,则不能偷该用户了
					String sneakSql = "SELECT count(1) num FROM sc_huodong_farm_user_sneak_log WHERE event_code = '"+event_code+"' AND member_code = '"+memberCode+"' AND stolen_code = '"+othersMemberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
					Map<String, Object> sneak = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(sneakSql, new MDataMap());
					int sneakCount = 0;
					if(null != sneak) {
						sneakCount = MapUtils.getIntValue(sneak,"num");
					}
					if(sneakCount <= 0) { 
						// 没偷过,则不能偷该用户了
						friendStealFlag = false;
					}
				}
				// 别人的水滴信息
				String userWaterSql = "SELECT * FROM sc_huodong_farm_user_water WHERE member_code = '"+othersMemberCode+"' AND event_code = '"+event_code+"' AND flag = '1'";
				List<Map<String, Object>> userWaterList = DbUp.upTable("sc_huodong_farm_user_water").dataSqlList(userWaterSql, new MDataMap());
				if(null != userWaterList && userWaterList.size() > 0) {
					for (Map<String, Object> map : userWaterList) {
						FarmWater farmWater = new FarmWater();
						String water_code = (String) map.get("water_code");
						int water_num = (int) map.get("water_num");
						String stealFlag = "0";
						
						if(friendStealFlag) { // 可以偷该用户
							// 水滴保底克数
							MDataMap waterEndMap = DbUp.upTable("sc_huodong_farm_config").one("type","449748480014");
							int endWater = MapUtils.getIntValue(waterEndMap,"begin_num");
							if(water_num <= endWater) {
								// 水滴保底3g
								stealFlag = "0";
							}else {
								// 看今天有没有偷过这个水滴
								String sneakLogSql = "SELECT * FROM sc_huodong_farm_user_sneak_log WHERE stolen_water_code = '"+water_code+"' AND member_code = '"+memberCode+"' AND create_time >= '"+begin+"' AND create_time <= '"+end+"'";
								Map<String, Object> sneakLogMap = DbUp.upTable("sc_huodong_farm_user_sneak_log").dataSqlOne(sneakLogSql, new MDataMap());
								if(null != sneakLogMap) {
									// 有偷取记录,则不可偷取
									stealFlag = "0";
								}else {
									// 没有有偷取记录,则可偷取
									stealFlag = "1";
								}
							}
						}
						
						farmWater.setStealFlag(stealFlag);
						farmWater.setWateCode(water_code);
						farmWater.setWaterNum(water_num);
						
						farmWaterlist.add(farmWater);
					}
				}
				
				// 查询别人昵称
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+othersMemberCode+"'", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+othersMemberCode+"'", new MDataMap());
					othersNickName = (String) login_info.get("login_name");
					if(farmService.isPhone(othersNickName)) {
						othersNickName = othersNickName.substring(0, 3) + "****" + othersNickName.substring(7);
					}
				}else { // 如果昵称不是空
					othersNickName = (String) member_sync.get("nickname");
				}
				
			}
		}
		
		result.setKettle_code(kettleCode);
		result.setFarmEvent(farmEvent);
		result.setFarmTree(farmTree);
		result.setKettleWater(kettleWater);
		result.setList(farmWaterlist);
		result.setOthersNickName(othersNickName);
		
		new PageActiveService().active(memberCode, getChannelId(), "4497471600630003");
		
		return result;
	}

	
}
