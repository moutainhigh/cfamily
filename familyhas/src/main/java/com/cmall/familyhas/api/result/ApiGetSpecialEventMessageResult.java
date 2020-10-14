package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetSpecialEventMessageResult extends RootResultWeb{
	
	@ZapcomApi(value = "活动编号")
	private List<String> nickNameList = new ArrayList<String>();

	public List<String> getNickNameList() {
		return nickNameList;
	}

	public void setNickNameList(List<String> nickNameList) {
		this.nickNameList = nickNameList;
	}

}
