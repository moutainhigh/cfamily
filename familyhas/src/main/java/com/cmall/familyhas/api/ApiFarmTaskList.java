package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiFarmTaskListInput;
import com.cmall.familyhas.api.model.FarmTask;
import com.cmall.familyhas.api.result.ApiFarmTaskListResult;
import com.cmall.familyhas.service.FarmService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 农场任务列表接口
 * @remark 
 * @author 任宏斌
 * @date 2020年2月10日
 */
public class ApiFarmTaskList extends RootApiForToken<ApiFarmTaskListResult, ApiFarmTaskListInput>{

	private static final long TIMEOUT = 3000;
	private FarmService farmService = new FarmService();
	
	@Override
	public ApiFarmTaskListResult Process(ApiFarmTaskListInput inputParam, MDataMap mRequestMap) {
		ApiFarmTaskListResult result = new ApiFarmTaskListResult();
		String memberCode = getUserCode();
		String eventCode = inputParam.getEventCode();
		String today = DateUtil.getSysDateString();
		
		String taskLockKey = Constants.TASK_PREFIX + memberCode;
		String taskLockCode = "";
		try {
			long begin = System.currentTimeMillis();
			while("".equals(taskLockCode = KvHelper.lockCodes(1, taskLockKey))) {
				if((System.currentTimeMillis() - begin) > TIMEOUT) {
					result.setResultCode(0);
					result.setResultMessage("请求超时");
					return result;
				}
				
				Thread.sleep(1);
			}
			
			int existCount = DbUp.upTable("sc_huodong_farm_user_task")
					.count("event_code", eventCode, "member_code", memberCode, "create_time", today);
			
			//如果还没有任务 生成今天的任务
			if(0 == existCount)  farmService.createTask(eventCode, memberCode, today);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("系统异常");
			return result;
		}finally {
			if(!"".equals(taskLockCode)) KvHelper.unLockCodes(taskLockCode, taskLockKey);
		}
		
		String sql1 = "SELECT t.task_code,t.task_type,c.task_name,t.task_num,t.already_num,c.task_description,"
				+ "c.award_water_num,c.browse_second,t.task_status FROM systemcenter.sc_huodong_farm_user_task t "
				+ "LEFT JOIN systemcenter.sc_huodong_farm_task c ON t.task_type=c.task_type "
				+ "WHERE t.event_code=:event_code AND t.member_code=:member_code AND t.create_time=:create_time";
		
		List<Map<String, Object>> taskList = DbUp.upTable("sc_huodong_farm_user_task")
				.dataSqlList(sql1, new MDataMap("event_code", eventCode, "member_code", memberCode, "create_time", today));
		
		for (Map<String, Object> task : taskList) {
			String taskType = MapUtils.getString(task, "task_type");
			FarmTask farmTask = new FarmTask();
			farmTask.setTaskCode(MapUtils.getString(task, "task_code"));
			farmTask.setTaskName(MapUtils.getString(task, "task_name"));
			farmTask.setTaskDescription(MapUtils.getString(task, "task_description"));
			farmTask.setTaskType(taskType);
			farmTask.setTaskNum(MapUtils.getIntValue(task, "task_num"));
			farmTask.setAlreadyNum(MapUtils.getIntValue(task, "already_num"));
			farmTask.setAwardWaterNum(MapUtils.getIntValue(task, "award_water_num"));
			farmTask.setBrowseSecond(MapUtils.getIntValue(task, "browse_second"));
			if("449748470001".equals(taskType)) {
				farmTask.setProductCode(farmService.getTaskProductCode());
			}else if("449748470002".equals(taskType)) {
				farmTask.setPageLink(farmService.getTaskPageLink());
			}
			farmTask.setButtonStatus(MapUtils.getString(task, "task_status"));
			result.getTaskList().add(farmTask);
		}
		
		return result;
	}
}
