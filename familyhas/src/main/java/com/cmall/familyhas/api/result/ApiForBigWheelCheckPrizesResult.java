package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.DailyWinningInformation;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForBigWheelCheckPrizesResult extends RootResult {

	@ZapcomApi(value = "每日中奖信息")
	private List<DailyWinningInformation> list = new ArrayList<DailyWinningInformation>();

	public List<DailyWinningInformation> getList() {
		return list;
	}

	public void setList(List<DailyWinningInformation> list) {
		this.list = list;
	}
	
}
