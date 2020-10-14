package com.cmall.familyhas.api;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForPvLogInput;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webapi.RootResultWeb;

/** 
* @Author fufu
* @Time 2020-8-18 11:57:39 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPvLog extends RootApiForVersion<RootResultWeb, ApiForPvLogInput> {

	@Override
	public RootResultWeb Process(ApiForPvLogInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String userCode = "";
		if(getFlagLogin()) {
			userCode = getOauthInfo().getUserCode();
		}
		String phone = inputParam.getPhone();
		if(StringUtils.isNotEmpty(phone)&&StringUtils.isEmpty(userCode)) {//用户编号为空，但是手机号不为空的情况下统计
			MDataMap loginInfo = DbUp.upTable("mc_login_info").one("login_name",phone,"manage_code","SI2003");
			if(loginInfo != null && !loginInfo.isEmpty()) {
				userCode = loginInfo.get("member_code");
			}
		}
		String channelId = getChannelId();
		MDataMap insert =  new MDataMap();
		insert.put("uid",UUID.randomUUID().toString().replace("-","").trim());
		insert.put("user_code",userCode);
		insert.put("channel_id",channelId);
		insert.put("source_type",inputParam.getSourcePageType());
		insert.put("create_time",FormatHelper.upDateTime());
		DbUp.upTable("fh_pv_uv").dataInsert(insert);
		return result;
	}

}
