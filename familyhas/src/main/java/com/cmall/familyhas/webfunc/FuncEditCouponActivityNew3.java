package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改优惠券活动 迭代3
 * @author ligj
 * time:2015-06-02 15:21:00
 *
 */
public class FuncEditCouponActivityNew3 extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		String activityName = mDataMap.get("zw_f_activity_name");
		String remark = mDataMap.get("zw_f_remark");
		if (activityName.length() > 100) {
			//活动名称最长100个字符
			mResult.inErrorMessage(916421246,"活动名称",100);
			return mResult;
		}
		if (remark.length() > 100) {
			//活动名称最长100个字符
			mResult.inErrorMessage(916421246,"活动描述",100);
			return mResult;
		}
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
		
		//活动优惠券发放限额
		String assign_line = mDataMap.get("zw_f_assign_line");
		if(StringUtils.isEmpty(assign_line)) {
			mDataMap.put("zw_f_assign_line", "0.0");
		}
		String assign_line2 = mDataMap.get("zw_f_assign_line2");
		if(StringUtils.isEmpty(assign_line2)) {
			mDataMap.put("zw_f_assign_line2", "0");
		}
		String assign_line3 = mDataMap.get("zw_f_assign_line3");
		if(StringUtils.isEmpty(assign_line3)) {
			mDataMap.put("zw_f_assign_line3", "0");
		}
		if(StringUtils.isEmpty(mDataMap.get("zw_f_rebate_ratio"))) {
			mDataMap.put("zw_f_rebate_ratio", "0");
		}
		if(StringUtils.isEmpty(mDataMap.get("zw_f_validate_time"))) {
			mDataMap.put("zw_f_validate_time", "0");
		}
		if(StringUtils.equals("4497471600060005",mDataMap.get("zw_f_provide_type"))) {
			mDataMap.put("zw_f_provide_num", "99999999");
		}
		if (mResult.upFlagTrue()) {
			/* 获取当前修改人 */
			String updater = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updator", updater);
			
			//兑换码兑换重新获取发放份数
			if("4497471600060004".equals(mDataMap.get("zw_f_provide_type"))) {
				MDataMap oneWhere = DbUp.upTable("oc_activity").oneWhere("activity_code", "", "uid=:uid", "uid", mDataMap.get("zw_f_uid"));
				int count = DbUp.upTable("oc_coupon_redeem").count("activity_code",oneWhere.get("activity_code"));
				mDataMap.put("zw_f_provide_num", String.valueOf(count));
			}
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(916422120);
		}
		return mResult;
	}
}
