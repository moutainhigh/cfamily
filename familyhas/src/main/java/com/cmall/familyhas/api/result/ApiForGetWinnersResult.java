package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.BigWheelWinner;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetWinnersResult extends RootResult {

	@ZapcomApi(value="获奖名单")
	private List<BigWheelWinner> list = new ArrayList<BigWheelWinner>();

	public List<BigWheelWinner> getList() {
		return list;
	}

	public void setList(List<BigWheelWinner> list) {
		this.list = list;
	}

	
}
