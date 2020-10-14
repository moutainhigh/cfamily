package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForSmgProductClickInput;
import com.cmall.familyhas.api.result.ApiForSmgProductClickResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 记录扫码购的点击立即购买按钮记录
 */
public class ApiForSmgProductClick extends RootApiForVersion<ApiForSmgProductClickResult, ApiForSmgProductClickInput> {

	public ApiForSmgProductClickResult Process(ApiForSmgProductClickInput input,MDataMap mRequestMap) { 
		ApiForSmgProductClickResult result = new ApiForSmgProductClickResult();
		
		if(StringUtils.isBlank(input.getFormId()) || StringUtils.isBlank(input.getProductCode())) {
			return result;
		}
		
		// 去掉扫码购的特定前缀
		String productCode = input.getProductCode();
		if(productCode.startsWith("IC_SMG_")){
			productCode = productCode.substring(7);
		}
		
		// 检查节目表
		MDataMap map = DbUp.upTable("pc_tv").one("form_id",input.getFormId(), "good_id", productCode);
		if(map == null) {
			return result;
		}
		
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		
		// 如果当前没有登录信息则根据unionId查询一下是否有绑定关系
		if(StringUtils.isBlank(memberCode) && StringUtils.isNotBlank(input.getUnionId())) {
			MDataMap loginInfo = DbUp.upTable("mc_login_info").one("manage_code", "SI2003", "unionId", input.getUnionId());
			if(loginInfo != null) {
				memberCode = loginInfo.get("member_code");
			}
		}
		
		if(StringUtils.isBlank(memberCode)) {
			return result;
		}
		
		// 记录扫码购的点击明细
		if(DbUp.upTable("fh_smg_click_detail").count("member_code", memberCode, "form_id", input.getFormId(), "product_code", productCode) == 0) {
			DbUp.upTable("fh_smg_click_detail").dataInsert(new MDataMap(
						"form_id", input.getFormId(),
						"member_code", memberCode,
						"product_code", productCode,
						"form_end_date", map.get("form_end_date"),
						"flag", "0",
						"create_time", FormatHelper.upDateTime()
					));
		}
		
		return result;
	}
	
}
