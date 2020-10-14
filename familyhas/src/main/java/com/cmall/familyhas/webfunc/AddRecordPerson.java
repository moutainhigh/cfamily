package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加客服数据
 * @author zhouguohui
 *
 */
public class AddRecordPerson extends RootFunc{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		if(mResult.upFlagTrue()){
			String sysFormat = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			
			mAddMaps.put("record_code", WebHelper.upCode("KF"));
			mAddMaps.put("create_time", sysDateTime.format(new java.util.Date()));
			mAddMaps.put("result_code", "449747410001");
			mAddMaps.put("type_code", "449747420001");
		    DbUp.upTable("mc_record_person").dataInsert(mAddMaps);
		}
		return mResult;
	}
}
