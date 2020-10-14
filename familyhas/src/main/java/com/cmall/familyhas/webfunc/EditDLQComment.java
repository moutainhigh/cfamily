package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

public class EditDLQComment extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mDataMap.put("zw_f_rtn_time", format.format(new Date()));
		
		return super.funcDo(sOperateUid, mDataMap);
	}
	
}
