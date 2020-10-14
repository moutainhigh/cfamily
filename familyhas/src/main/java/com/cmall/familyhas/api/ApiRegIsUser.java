package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiRegIsUserInput;
import com.cmall.familyhas.api.result.ApiRegIsUserResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiRegIsUser extends RootApiForManage<ApiRegIsUserResult ,ApiRegIsUserInput>{

	public ApiRegIsUserResult Process(ApiRegIsUserInput inputParam,
			MDataMap mRequestMap) {
		ApiRegIsUserResult result = new ApiRegIsUserResult();
		MDataMap mp = new MDataMap();
		mp.put("manage_code", getManageCode());
		mp.put("login_name", inputParam.getMobile());
		MDataMap dataCount = DbUp.upTable("mc_login_info").one("manage_code",getManageCode(),"login_name",inputParam.getMobile());
		if(dataCount != null ) {
			result.setFlagUser(true);
			String login_pass = dataCount.get("login_pass");
			String member_code = dataCount.get("member_code");
			if(null != login_pass && !"".equals(login_pass)){//判断密码是否存在
				result.setHasPwd(true);
			}
			String sql = "SELECT account_code FROM groupcenter.gc_member_relation WHERE account_code IN ( SELECT account_code FROM membercenter.mc_member_info WHERE member_code ='"+member_code+"')";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_login_info").dataSqlList(sql, new MDataMap());
			if(dataSqlList.size() > 0) {
				result.setHasPMember(true);
			}
		} 
		return result;
	}

}
