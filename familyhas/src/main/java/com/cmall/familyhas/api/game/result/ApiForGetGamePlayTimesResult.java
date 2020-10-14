package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetGamePlayTimesResult extends RootResult {
	
	@ZapcomApi(value="可玩次数",remark="传入游戏编号，获取游戏可玩次数",require=1)
	private int times = 2;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	
	
}
