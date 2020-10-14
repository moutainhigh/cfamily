package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @Author fufu
* @Time 2020年7月2日 下午4:15:17 
* @Version 1.0
* <p>Description:</p>
*/
public class FuncForOperateCategoryIgnore extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String uid = mDataMap.get("zw_f_uid");
		MDataMap mapInfo = DbUp.upTable("uc_ignore_category").one("uid",uid);
		if(mapInfo != null && !mapInfo.isEmpty()) {
			if("N".equals(mapInfo.get("if_ignore"))) {
				mapInfo.put("if_ignore", "Y");
			}else if("Y".equals(mapInfo.get("if_ignore"))){
				mapInfo.put("if_ignore", "N");
			}
			DbUp.upTable("uc_ignore_category").dataUpdate(mapInfo, "if_ignore", "uid");
		}
		return new MWebResult();
	}

}
