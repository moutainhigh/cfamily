package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiBatchDelColumnContentInput extends RootInput{
	@ZapcomApi(value="栏目内容uid",remark="",require=1)
	private List<String> uids = new ArrayList<String>();

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}
}
