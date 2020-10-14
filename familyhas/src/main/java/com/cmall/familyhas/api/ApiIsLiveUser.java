package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiIsLiveUserInput;
import com.cmall.familyhas.api.result.ApiIsLiveUserResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 
 * 类: ApiIsLiveUser <br>
 * 描述: 判断用户是否是主播用户 <br>
 * 作者: 付强 fuqiang@huijiayou.cn<br>
 * 时间: 2016-8-2 下午4:26:43
 */
public class ApiIsLiveUser extends RootApiForManage<ApiIsLiveUserResult, ApiIsLiveUserInput> {
	
	@Override
	public ApiIsLiveUserResult Process(ApiIsLiveUserInput inputParam, MDataMap mRequestMap) {

		ApiIsLiveUserResult result = new ApiIsLiveUserResult();
		if (StringUtils.isNotBlank(inputParam.getMobile())) {

			MDataMap one = DbUp.upTable("mc_login_info").one("login_name", inputParam.getMobile(), "manage_code",
					"SI2003");
			if (null != one) {

				WebClientSupport support = new WebClientSupport();
				try {
					// 中转直播项目的直播列表接口

					String sResponseString = support.doGet(bConfig("cfamily.isHadliveUserUrl") +"&memberCode=" + String.valueOf(one.get("member_code")));
					JSONObject parseObject = JSONObject.parseObject(sResponseString);
					result.setLiveUser(parseObject.getBooleanValue("isExist"));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return result;
	}

	
}
