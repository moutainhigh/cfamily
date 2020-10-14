package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForBuyerShowReadInput;
import com.cmall.familyhas.api.result.ApiForBuyerShowReadResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 买家秀阅读接口
 * @author lgx
 * 
 */
public class ApiForBuyerShowRead extends RootApiForVersion<ApiForBuyerShowReadResult, ApiForBuyerShowReadInput> {
	
	
	public ApiForBuyerShowReadResult Process(ApiForBuyerShowReadInput inputParam, MDataMap mRequestMap) {
		ApiForBuyerShowReadResult result = new ApiForBuyerShowReadResult();//返回结果
		
		// 调用渠道
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		
		String buyerShowUid = inputParam.getBuyerShowUid();
		
		if(!"".equals(userCode)) {			
			MDataMap one = DbUp.upTable("nc_buyer_show_read").one("buyer_show_uid",buyerShowUid,"member_code",userCode);
			if(one == null) {
				// 如果该用户没有阅读过则新增阅读记录
				MDataMap insertMap = new MDataMap();
				insertMap.put("buyer_show_uid", buyerShowUid);
				insertMap.put("member_code", userCode);
				insertMap.put("create_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("nc_buyer_show_read").dataInsert(insertMap);
			}
		}
		
		return result;
	}
	
	
}
