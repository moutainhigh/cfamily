package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiUserSignInput;
import com.cmall.familyhas.api.result.ApiUserSignResult;
import com.cmall.familyhas.service.PageActiveService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.ActivityInfos;
import com.cmall.ordercenter.model.CouponRelative;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiForUserSignSearch extends RootApiForToken<ApiUserSignResult, ApiUserSignInput> {

//	final Integer Cycle = Integer.parseInt(bConfig("familyhas.sign_cycle"));
	
	@Override
	public ApiUserSignResult Process(ApiUserSignInput inputParam, MDataMap mRequestMap) {

		Integer Cycle = getSignCycle();
		
		// 获取用户编号
		String member_code = getUserCode();
		ApiUserSignResult apiUserSignResult = new ApiUserSignResult();

		// 查询登录用户签到信息
		String sql = "select * from mc_sign where user_code =  '" + member_code + "'";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_sign").dataSqlList(sql, null);
		Map<String, Object> map = null;

		if (dataSqlList != null && dataSqlList.size() > 0) {
			map = dataSqlList.get(0);
		}

		// 获取相关优惠券信息
		//String sql1 = "select * from oc_coupon_relative_expand ORDER BY sign_days_get_coupon ";
		String nowTime = DateUtil.getNowTime();
		String sql1 = "SELECT	a.* FROM	oc_coupon_relative_expand a JOIN oc_activity b ON a.activity_code = b.activity_code WHERE b.flag = 1  and  b.begin_time < '"+nowTime+"' AND b.end_time > '"+nowTime+"' ORDER BY sign_days_get_coupon";
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

		// 如果用户获取查询结果
		if (map == null) {// 用户之前未签到过
			if (couponRelatives.size() == 0) {
				apiUserSignResult.setGetNextCouponDays(0);
				apiUserSignResult.setNextCouponName("无优惠券");
			} else {
				CouponRelative cr1 = new CouponRelative();// cr1为下一优惠券信息
				cr1.setSignSeqDays(Cycle+1);
				for (CouponRelative cr : couponRelatives) {
					if (cr.getSignSeqDays() > 0 && (cr.getSignSeqDays()) < (cr1.getSignSeqDays())) {
						cr1 = cr;
					}
				}
				apiUserSignResult.setGetNextCouponDays(cr1.getSignSeqDays());
				String activityCode = cr1.getActivityCode();
				String couponInfos = activeCodeTransferToCounponInfo(activityCode);
				couponInfos = split(couponInfos);
				apiUserSignResult.setNextCouponName(couponInfos);
			}
			apiUserSignResult.setFlagSignToday(0);
			apiUserSignResult.setSignSeqDays(0);
			apiUserSignResult.setSignRemindFlag("0");
		} else {// 用户之前有签到记录
			apiUserSignResult.setFlagSignToday((Integer) map.get("flag_sign_today"));
			apiUserSignResult.setGetNextCouponDays((Integer) map.get("get_next_coupon_need_days"));
			String nextActivityCode = map.get("next_activity_code").toString();
			String signRemindFlag = map.get("sign_remind_flag").toString();
			String couponInfos = activeCodeTransferToCounponInfo(nextActivityCode);
			couponInfos = split(couponInfos);
			apiUserSignResult.setNextCouponName(couponInfos);
			// 根据活动拼接返回优惠券数据
			apiUserSignResult.setSignRemindFlag(signRemindFlag);
			apiUserSignResult.setSignSeqDays((Integer) map.get("sign_seq_days"));
		}

		// 签到相关 连续签到天数对应优惠券
		List<ActivityInfos> list = new ArrayList<ActivityInfos>();
		for (CouponRelative cr : couponRelatives) {
			ActivityInfos activityInfos = new ActivityInfos();
			String activityCode = cr.getActivityCode();
			// 活动对应优惠券信息
			String couponInfos = activeCodeTransferToCounponInfo(activityCode);
			if(!couponInfos.contains(",")&&!"".equals(couponInfos)) {
				activityInfos.setJinEQuan(couponInfos);
			}else if(couponInfos.contains(",")&&!"".equals(couponInfos)){
				String[] split = couponInfos.split(",");
				if(!"0".equals(split[0])) {
					activityInfos.setJinEQuan(split[0]);
				}
				activityInfos.setZheKouQuan(split[1]);
			}
			activityInfos.setDays(cr.getSignSeqDays()+"");
			
			list.add(activityInfos);
		}
		apiUserSignResult.setActivityInfos(list);
		
		new PageActiveService().active(member_code, getChannelId(), "4497471600630002");
		
		return apiUserSignResult;
	}

	public Integer getSignCycle() {
		//先从缓存中 获取,如果没有获取到，走数据库，并存入缓存
		Integer cycle = 0;
		String signCycle = XmasKv.upFactory(EKvSchema.SignCycle).get("signCycle");
		if(null==signCycle) {
			//走数据库，并 存入缓存 
			String sql = "SELECT sign_days_get_coupon FROM `oc_coupon_relative_expand` ORDER BY sign_days_get_coupon DESC";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_relative_expand").dataSqlList(sql, null);
			if(null!=dataSqlList&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				String string = map.get("sign_days_get_coupon").toString();
				cycle = Integer.parseInt(string);
				XmasKv.upFactory(EKvSchema.SignCycle).set("signCycle",string);
			}
		}else {
			cycle = Integer.parseInt(signCycle);
		}
		return cycle;
	}

	public String activeCodeTransferToCounponInfo(String activityCode) {
		String sql2 = "select * from oc_coupon_type where activity_code = '" + activityCode + "' and  status = '4497469400030002'";
		List<Map<String, Object>> coupons = DbUp.upTable("oc_coupon_type").dataSqlList(sql2, null);
		StringBuilder sb = new StringBuilder();
		Integer totalMoney = 0;
		Integer countDiscounts = 0;
		for (Map<String, Object> mm : coupons) {
			if ("449748120001".equals(mm.get("money_type"))) {
				// 金额券
				totalMoney += ((BigDecimal) mm.get("money")).intValue();
			} else if ("449748120002".equals(mm.get("money_type"))) {
				countDiscounts += 1;
			}
		}
		if(totalMoney!=0) {
			sb.append(totalMoney);
		}else {
			sb.append(0);
		}
		if (countDiscounts != 0) {
			sb.append(","+countDiscounts);
		}
		return sb.toString();
	}
	
	public String split(String couponInfos) {
		String result = "";
		if(!couponInfos.contains(",")&&!"".equals(couponInfos)) {
			result += couponInfos+"元福利";
		}else if(couponInfos.contains(",")&&!"".equals(couponInfos)){
			String[] split = couponInfos.split(",");
			if(!"0".equals(split[0])) {
				result = result + split[0]+"元福利，";
			}
			result = result + split[1]+"张折扣券";
		}
		return result;
	}
}
