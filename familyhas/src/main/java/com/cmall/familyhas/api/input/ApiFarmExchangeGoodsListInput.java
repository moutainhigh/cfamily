package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiFarmExchangeGoodsListInput extends RootInput{

	@ZapcomApi(value = "活动编号")
	private String eventCode = "";
	@ZapcomApi(value = "果树编号")
	private String treeCode = "";
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getTreeCode() {
		return treeCode;
	}
	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	
}
