package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Program;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetProgramListResult  extends RootResult{

	@ZapcomApi(value="视频列表")
	private List<Program> proList = new ArrayList<Program>();

	public List<Program> getProList() {
		return proList;
	}

	public void setProList(List<Program> proList) {
		this.proList = proList;
	}
	
	
}
