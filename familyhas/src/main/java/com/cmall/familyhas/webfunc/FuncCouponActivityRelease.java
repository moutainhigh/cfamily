package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncCouponActivityRelease
 * @Description: 优惠券活动发布管理（发布/取消发布）
 * @author ligj
 * 
 */
public class FuncCouponActivityRelease extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String activity_code = mDataMap.get("zw_f_activity_code");
		MDataMap mdata = DbUp.upTable("oc_activity").oneWhere("flag,end_time","","","activity_code", activity_code);
		String flag = mdata.get("flag");
		
		MDataMap dataMap = new MDataMap();
		/* 获取当前修改人 */
		String updater = UserFactory.INSTANCE.create().getLoginName();
		if ("1".equals(flag)) {				// 已发布就设为未发布
			dataMap.put("flag", "0");
		} else if ("0".equals(flag)) {// 未发布就设为已发布
			//控制未发布的失效优惠活动不能设为已发布
			if (mdata.get("end_time").compareTo(DateUtil.getSysDateTimeString()) <= 0 ) {
				mResult.inErrorMessage(916401227);
				return mResult;
			}
			dataMap.put("flag", "1");
		}
		dataMap.put("updator", updater);
		dataMap.put("update_time", DateUtil.getSysDateTimeString());
		dataMap.put("activity_code", activity_code);
		
		DbUp.upTable("oc_activity").dataUpdate(dataMap,
				"flag,updator,update_time", "activity_code");
		return mResult;
	}

}
