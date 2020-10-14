package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiBatchDelColumnContentInput;
import com.cmall.familyhas.service.HomeColumnService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 批量标记删除栏目内容(js调用，后台首页板式栏目维护中维护内容页面下的批量删除按钮调用)
 * @author liguojie
 *
 */
public class ApiBatchDelColumnContent extends RootApi<RootResultWeb, ApiBatchDelColumnContentInput>{
	public RootResultWeb Process(ApiBatchDelColumnContentInput input,MDataMap mDataMap){
		RootResultWeb result = new RootResultWeb();
		new HomeColumnService().batchDelColumnContent(input.getUids());
		return result;
	}
}
