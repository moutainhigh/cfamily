package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForSubmitBrowserProductResult extends RootResultWeb {
	
	@ZapcomApi(value="返回剩余次数",remark="还剩多少次任务")
	private int residualTimes;

	public int getResidualTimes() {
		return residualTimes;
	}

	public void setResidualTimes(int residualTimes) {
		this.residualTimes = residualTimes;
	}

}
