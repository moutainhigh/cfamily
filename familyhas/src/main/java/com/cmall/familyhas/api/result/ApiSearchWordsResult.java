package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.SearchWords;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiSearchWordsResult extends RootResult {
	@ZapcomApi(value = "搜索配置结果集合", remark = "搜索配置结果集合只返回调用接口时间在开始时间和结束时间之内的数据")
	List<SearchWords> list = new ArrayList<SearchWords>();
	@ZapcomApi(value = "推广收益是否显示", remark = "0否  1 是")
    private Integer promote_is_show;

	public List<SearchWords> getList() {
		return list;
	}

	public void setList(List<SearchWords> list) {
		this.list = list;
	}

	public Integer getPromote_is_show() {
		return promote_is_show;
	}

	public void setPromote_is_show(Integer promote_is_show) {
		this.promote_is_show = promote_is_show;
	}
	
	
	
}
