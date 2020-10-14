package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 置底评价
 * @author lgx
 *
 */
public class FuncShoutDownEvaluation extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		mResult.setResultCode(1);
		
		String tplUid = mDataMap.get("zw_f_uid");
		MDataMap dataMap = new MDataMap();
		dataMap.put("uid", tplUid);
		String isDisable = mDataMap.get("zw_f_isDisable");
		if("1".equals(isDisable)){
			mResult.setResultCode(-1);
			mResult.setResultMessage("评价已经置底！");
		}else{
			dataMap.put("is_down", "1");
			DbUp.upTable("nc_order_evaluation").dataUpdate(dataMap, "is_down", "uid");
		}
		return mResult;
	}

	
}
