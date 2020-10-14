package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class SellerCategoryResult extends RootResultWeb{
	@ZapcomApi(value = "数据返回状态：success|error")
	private String status = "success";
	@ZapcomApi(value = "返回数据集合")
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}










