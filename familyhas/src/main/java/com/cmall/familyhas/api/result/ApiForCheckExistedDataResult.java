package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForCheckExistedDataResult extends RootResult{

	private List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}


	}
	

