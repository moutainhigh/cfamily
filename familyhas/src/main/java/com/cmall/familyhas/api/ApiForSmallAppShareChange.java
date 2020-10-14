package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForSmallAppShareChangeInput;
import com.cmall.familyhas.api.result.ApiForSmallAppShareChangeResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 小程序分享转换接口
 * @author lgx
 *
 */
public class ApiForSmallAppShareChange extends RootApiForVersion<ApiForSmallAppShareChangeResult, ApiForSmallAppShareChangeInput> {

	public ApiForSmallAppShareChangeResult Process(ApiForSmallAppShareChangeInput inputParam, MDataMap mRequestMap) {
		
		ApiForSmallAppShareChangeResult result = new ApiForSmallAppShareChangeResult();
		
		String shortContent = inputParam.getShortContent();
		
		String nowTime = FormatHelper.upDateTime();
		
		MDataMap mDataMap = new MDataMap();
		// 小程序推广赚分享参数唯一编号
		String shortCode = WebHelper.upCode("F");
		mDataMap.put("code", shortCode);
		mDataMap.put("content", shortContent);
		mDataMap.put("create_time", nowTime);
		DbUp.upTable("fh_short_code").dataInsert(mDataMap );
		
		result.setShortCode(shortCode);
		
		return result;
	}


}
