package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetFenXiaoMoneyInput extends RootInput {
	@ZapcomApi(value = "提现金额",require = 1)
    private int money = 0;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	

	
	
}
