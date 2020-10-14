package com.cmall.familyhas.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.api.ApiForUserSignSearch;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.CouponRelative;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForSignReset extends RootJob{

//	final Integer Cycle = Integer.parseInt(bConfig("familyhas.sign_cycle"));
	
	@Override
	public void doExecute(JobExecutionContext context) {
		
		Integer Cycle = new ApiForUserSignSearch().getSignCycle();
		
		//集合用户更新今日未签到用户常规状态
		MDataMap mdata = new MDataMap();
		mdata.put("sign_seq_days", "0");
		mdata.put("sign_get_activity_code", "");
		mdata.put("flag_sign_today", "0");
		// 获取相关优惠券信息
//		String sql1 = "select * from oc_coupon_relative_expand";
		String nowTime = DateUtil.getNowTime();
		String sql1 = "SELECT	a.* FROM	oc_coupon_relative_expand a JOIN oc_activity b ON a.activity_code = b.activity_code WHERE b.flag = 1  and  b.begin_time < '"+nowTime+"' AND b.end_time > '"+nowTime+"'";
		List<Map<String, Object>> couponList = DbUp.upTable("oc_coupon_relative_expand").dataSqlList(sql1, null);
		// couponList为签到相关所有活动信息
		List<CouponRelative> couponRelatives = new ArrayList<>();
		if (couponList != null) {
			for (Map<String, Object> ma1 : couponList) {
				CouponRelative couponRelative = new CouponRelative();
				couponRelative.setActivityCode(ma1.get("activity_code").toString());
				couponRelative.setSignSeqDays((Integer) ma1.get("sign_days_get_coupon"));
				couponRelatives.add(couponRelative);
			}
		}
		if (couponRelatives.size() == 0) {
			mdata.put("get_next_coupon_need_days", "");
			mdata.put("next_activity_code", "");
		} else {
			CouponRelative cr1 = new CouponRelative();// cr1为下一优惠券信息
			cr1.setSignSeqDays(Cycle+1);
			for (CouponRelative cr : couponRelatives) {
				if (cr.getSignSeqDays() > 0 && (cr.getSignSeqDays()) < (cr1.getSignSeqDays())) {
					cr1 = cr;
				}
			}
			mdata.put("get_next_coupon_need_days", cr1.getSignSeqDays()+"");
			mdata.put("next_activity_code", cr1.getActivityCode());
		}
		
		//0点更新用户签到状态
		//第一步 将今日未签到会员连续签到天数更为0，今日获得活动码、获取下一活动码信息更新
		DbUp.upTable("mc_sign").dataUpdate(mdata, "sign_seq_days,sign_get_activity_code,get_next_coupon_need_days,next_activity_code", "flag_sign_today");
		//第二步 将所有会员今日签到状态更为0,今日签到时间重置
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("flag_sign_today", "0");
		mDataMap.put("sign_time", "");
		DbUp.upTable("mc_sign").dataUpdate(mDataMap, "flag_sign_today,sign_time", "");
	}

}
