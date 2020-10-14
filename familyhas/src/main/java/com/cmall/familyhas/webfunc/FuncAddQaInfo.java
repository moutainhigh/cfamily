package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * @remark 客服与帮助-问题 添加
 * @author 任宏斌
 * @date 2018年12月3日
 */
public class FuncAddQaInfo extends FuncAdd{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		mDataMap.put("zw_f_qa_code", WebHelper.upCode("KB"));
		mDataMap.put("zw_f_create_user", UserFactory.INSTANCE.create().getLoginName());
		mDataMap.put("zw_f_create_time", DateHelper.upNow());
		mDataMap.put("zw_f_update_user", UserFactory.INSTANCE.create().getLoginName());
		mDataMap.put("zw_f_update_time", DateHelper.upNow());
		mDataMap.put("zw_f_qa_status", "449748270001"); //新增即发布
		mDataMap.put("zw_f_publish_time", DateHelper.upNow());
		return super.funcDo(sOperateUid, mDataMap);
		
	}

}