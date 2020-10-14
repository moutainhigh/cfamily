package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiFarmFinishTaskInput;
import com.cmall.familyhas.api.result.ApiFarmFinishTaskResult;
import com.cmall.familyhas.service.FarmService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 农场完成任务列表接口
 * @remark 
 * @author 任宏斌
 * @date 2020年2月10日
 */
public class ApiFarmFinishTask extends RootApiForToken<ApiFarmFinishTaskResult, ApiFarmFinishTaskInput>{

	private FarmService farmService = new FarmService();
	
	@Override
	public ApiFarmFinishTaskResult Process(ApiFarmFinishTaskInput inputParam, MDataMap mRequestMap) {
		ApiFarmFinishTaskResult result = new ApiFarmFinishTaskResult();
		
		String taskCode = inputParam.getTaskCode();
		MDataMap task = DbUp.upTable("sc_huodong_farm_user_task").one("task_code", taskCode);
		
		if(null == task) {
			result.setResultCode(91642604);
			result.setResultMessage(bInfo(91642604));
			return result;
		}
		
		String createTime = task.get("create_time");
		if(!DateUtil.getSysDateString().equals(createTime)) {
			result.setResultCode(91642605);
			result.setResultMessage(bInfo(91642605));
			return result;
		}
		
		String taskStatus = task.get("task_status");
		String newTaskStatus = "0";
		int newAlreayNum = 0;
		
		if("1".equals(taskStatus)) {
			result.setResultCode(91642608);
			result.setResultMessage(bInfo(91642608));
			return result;
		}else if ("0".equals(taskStatus)) {
			int taskNum = Integer.parseInt(task.get("task_num"));
			int alreayNum = Integer.parseInt(task.get("already_num"));
			newAlreayNum = alreayNum + 1;
			if(taskNum == newAlreayNum) {
				newTaskStatus = "1";
			}
		}
		
		//赠送水滴
		MDataMap taskConfig = DbUp.upTable("sc_huodong_farm_task").one("task_type", task.get("task_type"));
		String awardWaterNum = taskConfig.get("award_water_num");
		RootResult rootResult = farmService.sendWater2Kettle(task.get("event_code"), getUserCode(), awardWaterNum);
		if(rootResult.getResultCode() != 1) {
			result.inOtherResult(rootResult);
			return result;
		}
		
		//修改任务状态、次数
		DbUp.upTable("sc_huodong_farm_user_task").dataUpdate(
				new MDataMap("task_code", taskCode, "task_status", newTaskStatus, "already_num", newAlreayNum + ""),
				"task_status,already_num", "task_code");
		
		//生成日志
		DbUp.upTable("sc_huodong_farm_log").insert(
				"uid", WebHelper.upUuid(),
				"event_code", task.get("event_code"),
				"member_code", getUserCode(),
				"description", FormatHelper.formatString(bConfig("familyhas.farm_finish_task"), taskConfig.get("task_name")),
				"create_time", DateUtil.getSysDateTimeString(),
				"water_num", "+" + awardWaterNum);
		
		return result;
	}
	
}
