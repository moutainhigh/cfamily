package com.cmall.familyhas.webfunc.agent;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncForGetAgentMemberInfo extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String member_code = mDataMap.get("member_code");
		MDataMap res = DbUp.upTable("fh_agent_member_info").one("member_code",member_code);
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("member_code",member_code);
		MWebResult result = new MWebResult();
		if(res != null && !res.isEmpty() && loginInfo != null && !loginInfo.isEmpty()) {
			res.put("login_name", loginInfo.get("login_name"));
			result.setResultCode(1);
			result.setResultObject(res);
		}else {
			result.setResultCode(0);
		}
		return result;
	}

}
