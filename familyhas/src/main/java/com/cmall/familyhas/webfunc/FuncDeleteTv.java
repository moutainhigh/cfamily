package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDeleteTv extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mLogMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);		//插入到log里面的数据map
		mLogMaps = DbUp.upTable("pc_tv").one("uid",mLogMaps.get("uid"));
				
		int count = DbUp.upTable("pc_tv").delete("uid",mLogMaps.get("uid"));
		//记录操作日志
		if (count == 1) {
			MUserInfo userInfo = null;
			String userName = "";
			if (UserFactory.INSTANCE != null) {
				try {
					userInfo = UserFactory.INSTANCE.create();
				} catch (Exception e) {
				}
				if (userInfo != null) {
					userName = userInfo.getLoginName();
				}
			}
			
			MDataMap insertLog = new MDataMap();
			insertLog.put("form_id", mLogMaps.get("form_id"));
			insertLog.put("good_id", mLogMaps.get("good_id"));
			insertLog.put("opr_time", FormatHelper.upDateTime());
			insertLog.put("opr_user", userName);
			insertLog.put("json_data", mLogMaps.toString());
			DbUp.upTable("lc_tv_del").dataInsert(insertLog);
			
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

}
