package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForAgentShareInput;
import com.cmall.familyhas.api.result.ApiForAgentShareResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 分销人分享统计接口
 */
public class ApiForAgentShare extends RootApiForVersion<ApiForAgentShareResult, ApiForAgentShareInput> {

	@Override
	public ApiForAgentShareResult Process(ApiForAgentShareInput inputParam, MDataMap mRequestMap) {
		ApiForAgentShareResult apiResult = new ApiForAgentShareResult();

		if (!getFlagLogin()) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("登录信息过期，请重新登录！");
			return apiResult;
		}
		
		String memberCode = getOauthInfo().getUserCode();
		
		// 只统计分销人的分享
		if(DbUp.upTable("fh_agent_member_info").count("member_code", memberCode) == 0) {
			return apiResult;
		}

		if (StringUtils.isNotBlank(inputParam.getProductCode())) {
			MDataMap dataMap = new MDataMap();
			dataMap.put("memer_code", memberCode);
			dataMap.put("product_code", StringUtils.trimToEmpty(inputParam.getProductCode()));
			dataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("fh_agent_share_log").dataInsert(dataMap);
		}

		return apiResult;
	}

}
