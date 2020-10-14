package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiBatchDelRelevanceCommodityInput;
import com.cmall.familyhas.service.RelevanceCommodityService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 专题模板批量标记删除关联商品(js调用，活动设置—专题模板—模板-两栏多行模版下的批量删除按钮调用)
 * @author renhongbin
 *
 */
public class ApiBatchDelRelevanceCommodity extends RootApi<RootResultWeb, ApiBatchDelRelevanceCommodityInput>{
	public RootResultWeb Process(ApiBatchDelRelevanceCommodityInput input,MDataMap mDataMap){
		RootResultWeb result = new RootResultWeb();
		new RelevanceCommodityService().batchDelRelevanceCommodity(input.getUids());
		return result;
	}
}
