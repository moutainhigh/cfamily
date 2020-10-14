package com.cmall.familyhas.api.game;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.game.input.ApiForGetTaskInfoListInput;
import com.cmall.familyhas.api.game.result.ApiForGetTaskInfoListResult;
import com.cmall.familyhas.api.game.result.ApiForGetTaskInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 用户获取任务列表
 * 查询可完成任务
 * sc_hudong_game_taskinfo 查询任务表数据
 * @author Angel Joy
 *
 */
public class ApiForGetTaskInfoList extends RootApiForVersion<ApiForGetTaskInfoListResult,ApiForGetTaskInfoListInput>{

	@Override
	public ApiForGetTaskInfoListResult Process(ApiForGetTaskInfoListInput inputParam, MDataMap mRequestMap) {
		ApiForGetTaskInfoListResult result = new ApiForGetTaskInfoListResult();
		String gameCode = inputParam.getGameCode();
		String sql = "SELECT * FROM systemcenter.sc_hudong_game_taskinfo WHERE game_code = '"+gameCode+"' order by task_type asc";
		List<Map<String,Object>> taskList = DbUp.upTable("sc_hudong_game_taskinfo").dataSqlList(sql, null);
		if(taskList == null || taskList.size()<=0) {
			result.setResultCode(999999);
			result.setResultMessage("暂无任务可做");
			return result;
		}
		for(Map<String,Object> map : taskList) {
			ApiForGetTaskInfoResult info = new ApiForGetTaskInfoResult();
			info.setTaskDesc(StringUtils.trimToEmpty(map.get("task_desc").toString()));
			info.setGameTimes(StringUtils.trimToEmpty(map.get("add_times").toString()));
			info.setRepeatTimes(StringUtils.trimToEmpty(map.get("repeat_times").toString()));
			info.setTaskType(StringUtils.trimToEmpty(map.get("task_type").toString()));
			info.setTaskStatus("4497471600540001");//默认去完成
			result.getTaskList().add(info);
		}
		if(getFlagLogin()) {//用户登录的话，需要按照任务类型去查询用户的任务状态
			String userCode = getOauthInfo().getUserCode();
			String today = DateUtil.getNoSpSysDateString2();//获取今天日期 yyyy-MM-dd
			for(ApiForGetTaskInfoResult info : result.getTaskList()) {
				String type = info.getTaskType();
				String repeatTimes = info.getRepeatTimes();
				Integer repeatTimesInt = Integer.parseInt(repeatTimes);//任务可重复次数
				Integer complateTimes = 0;//用户完成任务数
				Integer getTimes = 0;//用户已领取次数
				if("4497471600530001".equals(type)) {//邀请好友喂青蛙
					String shareSql = "SELECT * FROM systemcenter.sc_hudong_game_share WHERE share_user_code = '"+userCode+"' AND game_code =  '"+gameCode+"' AND create_time >= '"+today+" 00:00:00'";
					List<Map<String,Object>> shareMapList = DbUp.upTable("sc_hudong_game_share").dataSqlList(shareSql, null);
					if(shareMapList != null && shareMapList.size()>=0) {//证明用户做过任务了。
						if(shareMapList.size()>repeatTimesInt) {
							complateTimes = repeatTimesInt;
						}else {
							complateTimes = shareMapList.size();
						}
					}
				}else if("4497471600530002".equals(type)) {//下载APP
					MDataMap userInfo = DbUp.upTable("mc_login_info").one("member_code",userCode);
					String mobile = userInfo.get("login_name");
					String downTimeSql = "SELECT * FROM groupcenter.gc_recommend_info WHERE mobile = '"+mobile+"' AND recommend_time >= '"+today+" 00:00:00'";
					List<Map<String,Object>> downAppList = DbUp.upTable("gc_recommend_info").dataSqlList(downTimeSql, null);
					if(downAppList != null && downAppList.size()>=0) {//证明用户做过任务了。
						if(downAppList.size()>repeatTimesInt) {
							complateTimes = repeatTimesInt;
						}else {
							complateTimes = downAppList.size();
						}
					}
					
				}else if("4497471600530003".equals(type)) {//下单
					String orderSql = "SELECT * FROM ordercenter.oc_orderinfo WHERE buyer_code = '"+userCode+"' AND order_status != '4497153900010001' AND order_status != '4497153900010006' AND create_time >= '"+today+" 00:00:00'";
					List<Map<String,Object>> orderList = DbUp.upTable("oc_orderinfo").dataSqlList(orderSql, null);
					if(orderList != null && orderList.size()>=0) {//证明用户做过任务了。
						if(orderList.size()>repeatTimesInt) {
							complateTimes = repeatTimesInt;
						}else {
							complateTimes = orderList.size();
						}
					}
				}else if("4497471600530004".equals(type)) {//浏览一分钟商品
					String browseSql = "SELECT * FROM systemcenter.sc_hudong_browse_product WHERE game_code = '"+gameCode+"' AND user_code = '"+userCode+"' AND create_time >= '"+today+" 00:00:00'";
					List<Map<String,Object>> browseList = DbUp.upTable("sc_hudong_browse_product").dataSqlList(browseSql, null);
					if(browseList != null && browseList.size()>=0) {//证明用户做过任务了。
						if(browseList.size()>repeatTimesInt) {
							complateTimes = repeatTimesInt;
						}else {
							complateTimes = browseList.size();
						}
					}
				}
				MDataMap addTimeMap = DbUp.upTable("sc_hudong_event_task").one("user_code",userCode,"task_type",type,"game_code",gameCode,"create_time",today);
				if(addTimeMap != null && !addTimeMap.isEmpty()) {
					getTimes = Integer.parseInt(addTimeMap.get("add_times"));
				}
				if(getTimes < complateTimes) {//有未领取的任务奖励
					info.setTaskStatus("4497471600540002");
				}else if(complateTimes == repeatTimesInt) {//已完成
					info.setTaskStatus("4497471600540003");
				}
			}
			
		}
		return result;
	}


	
}
