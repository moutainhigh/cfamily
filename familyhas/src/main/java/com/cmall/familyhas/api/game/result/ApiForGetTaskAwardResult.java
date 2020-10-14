package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetTaskAwardResult extends RootResultWeb {
	/**
	 * 返回领取之后可用次数
	 */
	@ZapcomApi(value="可用次数",remark="返回用户领取任务奖励后的可用次数")
	private int times = 0;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	
	
}
