package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FarmDaily;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmDailyResult extends RootResult {
	
	@ZapcomApi(value = "农场动态集合")
	private List<FarmDaily> dailyList = new ArrayList<FarmDaily>();

	public List<FarmDaily> getDailyList() {
		return dailyList;
	}

	public void setDailyList(List<FarmDaily> dailyList) {
		this.dailyList = dailyList;
	}

	
}
