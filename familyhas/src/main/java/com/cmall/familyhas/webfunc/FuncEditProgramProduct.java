package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditProgramProduct extends FuncEdit{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MDataMap one = DbUp.upTable("fh_program_product").one("uid",mDataMap.get("zw_f_uid"));
		if(null != one) {
			MDataMap sortModel = DbUp.upTable("fh_program_product").one("sort",mDataMap.get("zw_f_sort"),"program_code",one.get("program_code"));
			String repSort = one.get("sort");
			if(null != sortModel) {
				sortModel.put("sort", repSort);
				DbUp.upTable("fh_program_product").update(sortModel );
			}
			one.put("sort", mDataMap.get("zw_f_sort"));
			DbUp.upTable("fh_program_product").update(one  );
		}
		
		return new MWebResult();
	}
	
}
