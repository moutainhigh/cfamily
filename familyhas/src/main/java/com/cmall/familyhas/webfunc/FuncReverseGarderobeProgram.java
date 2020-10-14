package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncReverseGarderobeProgram extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		MDataMap one = DbUp.upTable("fh_program").one("uid", mDataMap.get("zw_f_uid"));
		
		if(null != one) {
			String status = one.get("status");
			if("449746740001".equals(status)) {
				one.put("status", "449746740002");
			} else {
				one.put("status", "449746740001");
			}
			DbUp.upTable("fh_program").update(one);
			
		} else {
			result.setResultCode(0);
			result.setResultMessage("操作失败");
		}
		
		return result;
	}

}
