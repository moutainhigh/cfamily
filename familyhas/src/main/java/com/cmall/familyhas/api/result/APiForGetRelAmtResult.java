package com.cmall.familyhas.api.result;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiForGetRelAmtResult extends RootResultWeb {
	
	@ZapcomApi(value = "可用积分数")
	private int possAccmAmt = 0;

	@ZapcomApi(value = "可用储值金金额")
	private BigDecimal possPpcAmt = new BigDecimal(0);
	
	@ZapcomApi(value = "可用暂存款金额")
	private BigDecimal possCrdtAmt = new BigDecimal(0);

	public int getPossAccmAmt() {
		return possAccmAmt;
	}

	public void setPossAccmAmt(int possAccmAmt) {
		this.possAccmAmt = possAccmAmt;
	}

	public BigDecimal getPossPpcAmt() {
		return possPpcAmt;
	}

	public void setPossPpcAmt(BigDecimal possPpcAmt) {
		this.possPpcAmt = possPpcAmt;
	}

	public BigDecimal getPossCrdtAmt() {
		return possCrdtAmt;
	}

	public void setPossCrdtAmt(BigDecimal possCrdtAmt) {
		this.possCrdtAmt = possCrdtAmt;
	}
	
}
