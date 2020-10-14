package com.cmall.familyhas.api.result;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.topapi.RootResult;

public class ApiImportOrderResult extends RootResult {

	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

}
