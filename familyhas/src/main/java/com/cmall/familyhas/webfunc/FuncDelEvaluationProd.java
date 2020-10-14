package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDelEvaluationProd extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
				
		MDataMap updateDataMap = new MDataMap();
		updateDataMap.put("uid",  mDataMap.get("zw_f_uid"));
		updateDataMap.put("is_delete", "1");
		DbUp.upTable("fh_apphome_evaluation").dataUpdate(updateDataMap, "is_delete", "uid");
		
		return mResult;
	}

}
