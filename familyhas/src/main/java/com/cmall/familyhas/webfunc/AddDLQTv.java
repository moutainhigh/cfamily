package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd; 
import com.srnpr.zapweb.webmodel.MWebResult;

public class AddDLQTv extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult result = new MWebResult();
		
		//页面编号
		mDataMap.put("zw_f_tv_number", WebHelper.upCode("DLQTV"));
		
		//创建人
        String createUserName = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_create_user", createUserName);
		//创建时间
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		
		result = super.funcDo(sOperateUid, mDataMap);
		
		return result;
		
	}
}
