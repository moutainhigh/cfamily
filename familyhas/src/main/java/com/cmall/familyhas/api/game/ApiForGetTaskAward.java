package com.cmall.familyhas.api.game;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.game.input.ApiForGetTaskAwardInput;
import com.cmall.familyhas.api.game.result.ApiForGetTaskAwardResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户领取任务奖励接口
 * 
 * @author Angel Joy
 * @date 20190925
 */
public class ApiForGetTaskAward extends RootApiForToken<ApiForGetTaskAwardResult, ApiForGetTaskAwardInput> {

	@Override
	public ApiForGetTaskAwardResult Process(ApiForGetTaskAwardInput inputParam, MDataMap mRequestMap) {
		ApiForGetTaskAwardResult result = new ApiForGetTaskAwardResult();
		String userCode = getUserCode();
		String gameCode = inputParam.getGameCode();
		String type = inputParam.getTaskType();
		MDataMap info = DbUp.upTable("sc_hudong_game_taskinfo").one("game_code", gameCode, "task_type", type);
		String repeatTimes = info.get("repeat_times");
		Integer repeatTimesInt = Integer.parseInt(repeatTimes);// 任务可重复次数
		Integer complateTimes = 0;// 用户完成任务数
		Integer getTimes = 0;// 用户已领取次数
		Integer addTimes = Integer.parseInt(info.get("add_times"));// 奖励次数
		String today = DateUtil.getNoSpSysDateString2();
		if ("4497471600530001".equals(type)) {// 邀请好友喂青蛙
			String shareSql = "SELECT * FROM systemcenter.sc_hudong_game_share WHERE share_user_code = '" + userCode
					+ "' AND game_code =  '" + gameCode + "' AND create_time >= '" + today + " 00:00:00'";
			List<Map<String, Object>> shareMapList = DbUp.upTable("sc_hudong_game_share").dataSqlList(shareSql, null);
			if (shareMapList != null && shareMapList.size() >= 0) {// 证明用户做过任务了。
				if (shareMapList.size() > repeatTimesInt) {
					complateTimes = repeatTimesInt;
				} else {
					complateTimes = shareMapList.size();
				}
			}
		} else if ("4497471600530002".equals(type)) {// 下载APP
			MDataMap userInfo = DbUp.upTable("mc_login_info").one("member_code", userCode);
			String mobile = userInfo.get("login_name");
			String downTimeSql = "SELECT * FROM groupcenter.gc_recommend_info WHERE mobile = '" + mobile
					+ "' AND recommend_time >= '" + today + " 00:00:00'";
			List<Map<String, Object>> downAppList = DbUp.upTable("gc_recommend_info").dataSqlList(downTimeSql, null);
			if (downAppList != null && downAppList.size() >= 0) {// 证明用户做过任务了。
				if (downAppList.size() > repeatTimesInt) {
					complateTimes = repeatTimesInt;
				} else {
					complateTimes = downAppList.size();
				}
			}

		} else if ("4497471600530003".equals(type)) {// 下单
			String orderSql = "SELECT * FROM ordercenter.oc_orderinfo WHERE buyer_code = '" + userCode
					+ "' AND order_status != '4497153900010001' AND order_status != '4497153900010006'  AND create_time >= '" + today + " 00:00:00'";
			List<Map<String, Object>> orderList = DbUp.upTable("oc_orderinfo").dataSqlList(orderSql, null);
			if (orderList != null && orderList.size() >= 0) {// 证明用户做过任务了。
				if (orderList.size() > repeatTimesInt) {
					complateTimes = repeatTimesInt;
				} else {
					complateTimes = orderList.size();
				}
			}
		} else if ("4497471600530004".equals(type)) {// 浏览一分钟商品
			String browseSql = "SELECT * FROM systemcenter.sc_hudong_browse_product WHERE game_code = '"+gameCode+"' AND user_code = '" + userCode
					+ "' AND create_time >= '" + today + " 00:00:00'";
			List<Map<String, Object>> browseList = DbUp.upTable("sc_hudong_browse_product").dataSqlList(browseSql,
					null);
			if (browseList != null && browseList.size() >= 0) {// 证明用户做过任务了。
				if (browseList.size() > repeatTimesInt) {
					complateTimes = repeatTimesInt;
				} else {
					complateTimes = browseList.size();
				}
			}
		}
		MDataMap addTimeMap = DbUp.upTable("sc_hudong_event_task").one("user_code", userCode, "task_type", type,
				"game_code", gameCode, "create_time", today);
		if (addTimeMap != null && !addTimeMap.isEmpty()) {
			getTimes = Integer.parseInt(addTimeMap.get("add_times"));
		}
		if (getTimes < complateTimes) {// 有未领取的任务奖励
			if (addTimeMap != null && !addTimeMap.isEmpty()) {// 当日已经有过领取记录，更新次数
				// 需要更新用户领取次数表
				addTimeMap.put("add_times", complateTimes.toString());// 将任务次数变更为已完成次数。
				DbUp.upTable("sc_hudong_event_task").dataUpdate(addTimeMap, "add_times", "uid");
			} else {
				// 需要新插入用户表领取记录
				addTimeMap = new MDataMap();
				addTimeMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
				addTimeMap.put("game_code", gameCode);
				addTimeMap.put("task_type", type);
				addTimeMap.put("user_code", userCode);
				addTimeMap.put("add_times", complateTimes.toString());
				addTimeMap.put("create_time", today);
				DbUp.upTable("sc_hudong_event_task").dataInsert(addTimeMap);
			}
			// 需要更新用户用户次数表数据。
			String gameTimes = "0";
			Integer awardTimes = (new BigDecimal(complateTimes).subtract(new BigDecimal(getTimes))
					.multiply(new BigDecimal(addTimes))).intValue();
			MDataMap gameUserInfo = DbUp.upTable("sc_hudong_game_user").one("user_code", userCode, "game_code",
					gameCode);// 查询用户今日的可玩次数
			if (gameUserInfo != null && !gameUserInfo.isEmpty()) {
				String dateTime = gameUserInfo.get("date_time");
				if (today.equals(dateTime)) {// 今天的数据
					gameTimes = gameUserInfo.get("game_times");
					gameTimes = (new BigDecimal(awardTimes).add(new BigDecimal(gameTimes))).toString();
					gameUserInfo.put("game_times", gameTimes);
					DbUp.upTable("sc_hudong_game_user").dataUpdate(gameUserInfo, "game_times", "uid");
				} else {
					gameTimes = bConfig("familyhas.game_times");// 获取配置文件的次数
					gameTimes = (new BigDecimal(awardTimes).add(new BigDecimal(gameTimes))).toString();
					gameUserInfo.put("game_times", gameTimes);
					gameUserInfo.put("date_time", today);
					DbUp.upTable("sc_hudong_game_user").dataUpdate(gameUserInfo, "game_times,date_time", "uid");
				}
			} else {
				// 需要插入一条数据
				MDataMap gameInfo = DbUp.upTable("sc_hudong_game").one("game_code", gameCode);
				if (gameInfo == null || gameInfo.isEmpty()) {
					result.setResultCode(999999);
					result.setResultMessage("游戏不存在！！！");
					return result;
				}
				gameTimes = bConfig("familyhas.game_times");// 获取配置文件的次数
				gameTimes = (new BigDecimal(awardTimes).add(new BigDecimal(gameTimes))).toString();
				gameUserInfo.put("game_times", gameTimes);
				MDataMap insert = new MDataMap();
				insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
				insert.put("game_code", gameCode);
				insert.put("user_code", userCode);
				insert.put("game_name", gameInfo.get("game_name"));
				insert.put("game_times", gameTimes);
				insert.put("date_time", today);
				DbUp.upTable("sc_hudong_game_user").dataInsert(insert);
			}
			result.setTimes(Integer.parseInt(gameTimes));
			result.setResultCode(1);
			result.setResultMessage("领取成功");
		} else {
			result.setResultCode(999999);
			result.setResultMessage("未检测到完成任务或是任务奖励已领取，请联系管理员");
		}
		return result;

	}

}
