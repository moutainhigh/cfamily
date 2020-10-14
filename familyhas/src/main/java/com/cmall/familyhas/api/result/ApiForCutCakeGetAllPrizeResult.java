package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.CutCakeDrawJl;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCutCakeGetAllPrizeResult extends RootResult {

	@ZapcomApi(value = "中奖记录")
	private List<CutCakeDrawJl> list = new ArrayList<CutCakeDrawJl>();

	public List<CutCakeDrawJl> getList() {
		return list;
	}

	public void setList(List<CutCakeDrawJl> list) {
		this.list = list;
	}
	
}
