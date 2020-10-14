package com.cmall.familyhas.api.result;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetShopCarAndCustAmtForCrmResult extends RootResult {

	@ZapcomApi(value = "可用积分")
	private int possAccmAmt = 0;

	@ZapcomApi(value = "可用储值金金额")
	private int possPpcAmt = 0;
	
	@ZapcomApi(value = "可用暂存款金额")
	private int possCrdtAmt = 0;
	
	@ZapcomApi(value = "购物车商品数")
	private int shoppingCarNum = 0;

	public int getPossAccmAmt() {
		return possAccmAmt;
	}

	public void setPossAccmAmt(int possAccmAmt) {
		this.possAccmAmt = possAccmAmt;
	}

	public int getPossPpcAmt() {
		return possPpcAmt;
	}

	public void setPossPpcAmt(int possPpcAmt) {
		this.possPpcAmt = possPpcAmt;
	}

	public int getPossCrdtAmt() {
		return possCrdtAmt;
	}

	public void setPossCrdtAmt(int possCrdtAmt) {
		this.possCrdtAmt = possCrdtAmt;
	}

	public int getShoppingCarNum() {
		return shoppingCarNum;
	}

	public void setShoppingCarNum(int shoppingCarNum) {
		this.shoppingCarNum = shoppingCarNum;
	}


}
