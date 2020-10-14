package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FarmTask;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiFarmTaskListResult extends RootResultWeb{

	@ZapcomApi(value = "任务列表")
	private List<FarmTask> taskList = new ArrayList<FarmTask>();

	public List<FarmTask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<FarmTask> taskList) {
		this.taskList = taskList;
	}
	
}
