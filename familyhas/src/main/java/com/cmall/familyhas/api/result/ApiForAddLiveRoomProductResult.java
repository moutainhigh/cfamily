package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.topapi.RootResult;

public class ApiForAddLiveRoomProductResult  extends RootResult {

	private int totalNum = 0;
	private int leftNum =0;
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getLeftNum() {
		return leftNum;
	}
	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}
	
	
	

}