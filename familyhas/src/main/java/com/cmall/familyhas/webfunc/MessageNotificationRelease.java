package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class MessageNotificationRelease extends RootFunc{

	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
	
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if (StringUtils.isEmpty(mAddMaps.get("uid"))) {
			mResult.setResultMessage("发布失败");
		}else {
			// 定义插入数据库
			MDataMap mInsertMap = new MDataMap();
			String uid = mAddMaps.get("uid");
			mInsertMap.put("uid",uid);
			mInsertMap.put("status", "4497469400030002");
			DbUp.upTable("fh_message_notification").dataUpdate(mInsertMap, "status", "uid");
		}
		return mResult;
	}

}
