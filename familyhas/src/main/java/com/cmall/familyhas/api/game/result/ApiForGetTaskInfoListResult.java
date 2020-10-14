package com.cmall.familyhas.api.game.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetTaskInfoListResult extends RootResultWeb {
	
	@ZapcomApi(value="任务列表",remark="")
	private List<ApiForGetTaskInfoResult> taskList = new ArrayList<ApiForGetTaskInfoResult>();

	public List<ApiForGetTaskInfoResult> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<ApiForGetTaskInfoResult> taskList) {
		this.taskList = taskList;
	}
	
	


}
