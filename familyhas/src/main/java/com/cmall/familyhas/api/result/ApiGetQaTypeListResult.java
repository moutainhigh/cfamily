package com.cmall.familyhas.api.result;

import java.util.List;

import com.cmall.familyhas.api.model.QaTypeModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetQaTypeListResult extends RootResult {

	@ZapcomApi(value="问题类型列表")
	private List<QaTypeModel> list;

	public List<QaTypeModel> getList() {
		return list;
	}

	public void setList(List<QaTypeModel> list) {
		this.list = list;
	}
	
}
