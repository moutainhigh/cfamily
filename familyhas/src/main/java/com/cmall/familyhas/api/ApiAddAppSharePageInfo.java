package com.cmall.familyhas.api;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiAddAppSharePageInfo extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MUserInfo sys = UserFactory.INSTANCE.create(); 
		String manageCode = sys.getManageCode();
		int count = DbUp.upTable("fh_app_share").count("manage_code",manageCode);
		
		
		if (count < 1) {
			DbUp.upTable("fh_app_share").insert("title",mDataMap.get("zw_f_title"),
					"picture",mDataMap.get("zw_f_picture"),
					"title_rule",mDataMap.get("zw_f_title_rule"),
					"description",mDataMap.get("zw_f_description"),
					"share_title",mDataMap.get("zw_f_share_title"),
					"share_description",mDataMap.get("zw_f_share_description"),
					"share_pic",mDataMap.get("zw_f_share_pic"),
//					"share_link",mDataMap.get("zw_f_share_link"),
					"manage_code",manageCode);
		} else {
			mResult.setResultCode(0);
			mResult.setResultMessage("只允许添加一条数据，可对上一条数据进行修改");
		}
		return mResult;
	}

}
