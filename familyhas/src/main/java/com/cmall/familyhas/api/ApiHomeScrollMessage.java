package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiHomeScrollMessageInput;
import com.srnpr.xmassystem.load.LoadHomeScrollMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessageQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 首页滚动消息接口
 * @remark 
 * @author 任宏斌
 * @date 2019年8月19日
 */
public class ApiHomeScrollMessage extends RootApiForVersion<PlusModelHomeScrollMessage, ApiHomeScrollMessageInput> {

	@Override
	public PlusModelHomeScrollMessage Process(ApiHomeScrollMessageInput inputParam, MDataMap mRequestMap) {
		return new LoadHomeScrollMessage().upInfoByCode(new PlusModelHomeScrollMessageQuery());
	}
	
}
