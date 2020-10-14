package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForGetStoreDistrictInput;
import com.cmall.familyhas.api.result.APiTestForIosResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 为IOS提供测试接口
 * 
 * @author xiegj
 *
 */
public class APiTestForIos extends RootApi<APiTestForIosResult, ApiForGetStoreDistrictInput> {

	public APiTestForIosResult Process(ApiForGetStoreDistrictInput inputParam,
			MDataMap mRequestMap) {
		APiTestForIosResult mWebResult = new APiTestForIosResult();
		MDataMap md = DbUp.upTable("fh_test_ios").one("zid","1");
		mWebResult.setValue(md.get("value"));
		return mWebResult;
	}

}
