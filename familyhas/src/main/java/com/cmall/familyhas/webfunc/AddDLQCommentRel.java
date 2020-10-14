package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class AddDLQCommentRel extends FuncAdd{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sysTime = format.format(new Date());
		mDataMap.put("zw_f_create_time", sysTime);
		mDataMap.put("zw_f_update_time", sysTime);
		mDataMap.put("zw_f_manage_code", "SI2003");
		mDataMap.put("zw_f_flg", "1000");
		return super.funcDo(sOperateUid, mDataMap);
		
	}

}
