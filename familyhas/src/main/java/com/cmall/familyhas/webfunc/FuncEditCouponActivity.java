package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改优惠券活动
 * @author ligj
 *
 */
public class FuncEditCouponActivity extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();

		String startTime = mDataMap.get("zw_f_begin_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if (endTime.compareTo(startTime) <= 0) {
			//开始时间必须小于结束时间!
			mResult.inErrorMessage(916401201);
			return mResult;
		}else if (endTime.compareTo(createTime) <= 0) {
			//当前时间必须小于结束时间!
			mResult.inErrorMessage(916401214);
			return mResult;
		}
		
		if (mResult.upFlagTrue()) {
			/* 获取当前修改人 */
			String updater = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updator", updater);
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
