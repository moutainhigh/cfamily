package com.cmall.familyhas.api;

import com.cmall.familyhas.api.ApiForConfirmProclamation.ConfirmProclamationResult;
import com.cmall.familyhas.api.input.ConfirmProclamationInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForConfirmProclamation extends RootApi<ConfirmProclamationResult, ConfirmProclamationInput> {

	@Override
	public ConfirmProclamationResult Process(ConfirmProclamationInput inputParam, MDataMap mRequestMap) {
		ConfirmProclamationResult result = new ConfirmProclamationResult();
		String userCode = inputParam.getUserCode();
		String proclamation = inputParam.getProclamationCode();
		String nowTime = DateUtil.getNowTime();
		MDataMap updateMap = new MDataMap();
		updateMap.put("user_code", userCode);
		updateMap.put("proclamation_code", proclamation);
		updateMap.put("confirm_time", nowTime);
		DbUp.upTable("fh_proclamation_confirmation").dataInsert(updateMap);
		return result;
	}

	
	class ConfirmProclamationResult extends RootResultWeb {
		
	}
	
	
	
}
