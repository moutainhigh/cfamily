package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class NewsNotificationReleaseCancel extends RootFunc{

	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
	
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if (StringUtils.isEmpty(mAddMaps.get("uid"))) {
			mResult.setResultMessage("取消发布失败");
		}else {
			// 定义插入数据库
			MDataMap mInsertMap = new MDataMap();
			String uid = mAddMaps.get("uid");
			mInsertMap.put("uid",uid);
			mInsertMap.put("status", "4497469400030001");
			DbUp.upTable("fh_news_notification").dataUpdate(mInsertMap, "status", "uid");
			MDataMap dataMap = new MDataMap();
			dataMap.put("uid",uid);
			Object proclamation_code = DbUp.upTable("fh_news_notification").dataGet("proclamation_code", "uid =:uid", dataMap);
			dataMap.clear();
			dataMap.put("proclamation_code", proclamation_code.toString());
			dataMap.put("proclamation_status", "4497477000010002");
			DbUp.upTable("fh_proclamation_manage").dataUpdate(dataMap,"proclamation_status","proclamation_code");
		}
		return mResult;
	}

}
