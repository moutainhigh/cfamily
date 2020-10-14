package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * @remark 客服与帮助-问题 修改
 * @author 任宏斌
 * @date 2018年12月3日
 */
public class FuncEditQa extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		mDataMap.put("zw_f_update_user", UserFactory.INSTANCE.create().getLoginName());
		mDataMap.put("zw_f_update_time", DateHelper.upNow());
		MWebResult mResult = super.funcDo(sOperateUid, mDataMap);
		
		return mResult;
	}
}
