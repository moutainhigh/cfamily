package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Reason;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForCancelShipmentsReasonResult extends RootResult {

	@ZapcomApi(value = "原因列表", require = 1)
	private List<Reason> reasonList=new ArrayList<Reason>();

	public List<Reason> getReasonList() {
		return reasonList;
	}

	public void setReasonList(List<Reason> reasonList) {
		this.reasonList = reasonList;
	}
	
	
}
