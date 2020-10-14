package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券活动
 * @author ligj
 *
 */
public class FuncAddCouponActivity extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String startTime = mDataMap.get("zw_f_begin_time");
		String endTime = mDataMap.get("zw_f_end_time");
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		
		if (endTime.compareTo(startTime) <= 0) {
			//开始时间必须小于结束时间!
			mResult.inErrorMessage(916401201);
			return mResult;
		}else if (endTime.compareTo(createTime) <= 0) {
//			当前时间必须小于结束时间!
			mResult.inErrorMessage(916401214);
			return mResult;
		}
		
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_creator", create_user);
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updator", create_user);
			mDataMap.put("zw_f_activity_code", WebHelper.upCode("AC"));
			mDataMap.put("zw_f_activity_type", "449715400007");
			mDataMap.put("zw_f_seller_code", "SI2003");
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
