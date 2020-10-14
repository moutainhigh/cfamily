package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.JFBigWheelWinner;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetJFWinnersResult extends RootResult {

	@ZapcomApi(value="获奖名单")
	private List<JFBigWheelWinner> list = new ArrayList<JFBigWheelWinner>();
	
	@ZapcomApi(value="奖品总人数")
	private int totalNum=0;

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<JFBigWheelWinner> getList() {
		return list;
	}

	public void setList(List<JFBigWheelWinner> list) {
		this.list = list;
	}

	
}
