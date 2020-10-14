package com.cmall.familyhas.api;

import java.util.List;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiGetCommentPictureCf extends
 RootApi<MWebResult, RootInput> {

	public MWebResult Process(RootInput inputParam, MDataMap mRequestMap) {
		
		MWebResult result = new MWebResult();
		List<MDataMap> commentMap = DbUp.upTable("nc_order_evaluation").queryAll("oder_photos", "", "uid=:uid", new MDataMap("uid",mRequestMap.get("zw_f_uid")));
		if(commentMap.size() != 0)
			result.setResultMessage(commentMap.get(0).get("oder_photos"));
		return result;
	}
}
