package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncCouponTypeRelease
 * @Description: 优惠券发布管理（发布/取消发布）
 * @author ligj
 * 
 */
public class FuncCouponTypeRelease extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String coupon_type_code = mDataMap.get("zw_f_coupon_type_code");
		MDataMap mdata = DbUp.upTable("oc_coupon_type").one("coupon_type_code", coupon_type_code);
		String status = mdata.get("status");
		
		MDataMap dataMap = new MDataMap();
		/* 获取当前修改人 */
		String updater = UserFactory.INSTANCE.create().getLoginName();
		if ("4497469400030002".equals(status)) {// 已发布就设为未发布
			dataMap.put("status", "4497469400030001");
		} else if ("4497469400030001".equals(status)) {// 未发布就设为已发布
			
			//控制未发布的失效优惠活动不能设为已发布
			if (mdata.get("end_time").compareTo(DateUtil.getSysDateTimeString()) <= 0 ) {
				mResult.inErrorMessage(916401222);
				return mResult;
			}else if (mdata.get("surplus_money").compareTo("0") <= 0) {
				mResult.inErrorMessage(916401223);
				return mResult;
			}
			dataMap.put("status", "4497469400030002");
		}
		dataMap.put("updater", updater);
		dataMap.put("update_time", DateUtil.getSysDateTimeString());
		dataMap.put("coupon_type_code", coupon_type_code);
		DbUp.upTable("oc_coupon_type").dataUpdate(dataMap,
				"status,updater,update_time", "coupon_type_code");
		return mResult;
	}

}
