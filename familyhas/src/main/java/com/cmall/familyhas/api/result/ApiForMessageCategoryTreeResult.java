package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.topapi.RootResult;

public class ApiForMessageCategoryTreeResult extends RootResult {
	
	public List<List<String>> list = new ArrayList<List<String>>();
	

	public List<List<String>> getList() {
		return list;
	}

	public void setList(List<List<String>> list) {
		this.list = list;
	}
	
	

}
