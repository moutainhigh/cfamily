package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForActivityCouponInput;
import com.cmall.familyhas.api.input.ApiUserSignInput;
import com.cmall.familyhas.api.result.ApiUserSignResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.CouponRelative;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForUserSign extends RootApiForToken<ApiUserSignResult, ApiUserSignInput> {

//	final Integer Cycle = Integer.parseInt(bConfig("familyhas.sign_cycle"));
	
	@Override
	public ApiUserSignResult Process(ApiUserSignInput inputParam, MDataMap mRequestMap) {
		Integer Cycle = new ApiForUserSignSearch().getSignCycle();
		// 获取用户编号
		String member_code = getUserCode();

		ApiUserSignResult apiUserSignResult = new ApiUserSignResult();

		// 查询登录用户签到信息
		String sql = "select * from mc_sign where user_code =  '" + member_code + "'";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_sign").dataSqlList(sql, null);
		Map<String, Object> map = null;

		// SignInfo signInfo = new SignInfo();
		MDataMap updateMap = new MDataMap();
		MDataMap log = new MDataMap();

		if (dataSqlList != null && dataSqlList.size() > 0) {
			map = dataSqlList.get(0);
		}

		// 获取相关优惠券信息
//		String sql1 = "select * from oc_coupon_relative_expand";
		String nowTime = DateUtil.getNowTime();
		String sql1 = "SELECT	a.* FROM	oc_coupon_relative_expand a JOIN oc_activity b ON a.activity_code = b.activity_code WHERE b.flag = 1  and  b.begin_time < '"+nowTime+"' AND b.end_time > '"+nowTime+"'";
		List<Map<String, Object>> couponList = DbUp.upTable("oc_coupon_relative_expand").dataSqlList(sql1, null);
		// couponList为签到相关所有活动信息
		List<CouponRelative> couponRelatives = new ArrayList<>();
		CouponRelative firstCoupon = new CouponRelative();
		firstCoupon.setSignSeqDays(Cycle+1);
		if (couponList != null) {
			for (Map<String, Object> ma1 : couponList) {
				CouponRelative couponRelative = new CouponRelative();
				couponRelative.setActivityCode(ma1.get("activity_code").toString());
				couponRelative.setSignSeqDays((Integer) ma1.get("sign_days_get_coupon"));
				if(couponRelative.getSignSeqDays()<firstCoupon.getSignSeqDays()) {
					firstCoupon = couponRelative;
				}
				couponRelatives.add(couponRelative);
			}
		}
		//加 锁  防止用户签到 太快引起的异常
		String sLockKey = KvHelper.lockCodes(10, member_code);
		if (StringUtils.isBlank(sLockKey)) {
			apiUserSignResult.setResultCode(0);
			apiUserSignResult.setResultMessage("签到太频繁，请稍后再试");
			return apiUserSignResult;
		} else {

			updateMap.put("user_code", member_code);

			log.put("user_code", member_code);
			log.put("sign_time", DateUtil.getNowTime());

			// 今日获取优惠券对应活动
			updateMap.put("sign_get_activity_code", "");
			// 获取下一优惠券信息
			updateMap.put("get_next_coupon_need_days", "");
			updateMap.put("next_activity_code", "");
			CouponRelative bingoCouponRelative = null;
			if (map == null) {// 用户以前未签到过,执行添加签到操作
				updateMap.put("sign_seq_days", "1");
				updateMap.put("flag_sign_today", "1");
				updateMap.put("sign_time", DateUtil.getNowTime());

				// 日志相关
				log.put("sign_seq_days", "1");
				apiUserSignResult.setSignSeqDays(1);
				// 今天是否获得优惠券
				

				// 判断第一天签到有无优惠券发送
				if (couponRelatives != null) {// 有 优惠券可用
					// 下一优惠券对象
					CouponRelative cr1 = new CouponRelative();
					cr1.setSignSeqDays(Cycle+1);
					for (CouponRelative cr : couponRelatives) {
						if (cr.getSignSeqDays() == 1) {
							bingoCouponRelative = cr;
							updateMap.put("sign_get_activity_code", cr.getActivityCode());
							log.put("get_activity_code", cr.getActivityCode());
							apiUserSignResult
									.setSignGetCouponName(new StringBuffer(activeCodeTransferToCounponInfo(cr.getActivityCode())).insert(0, "恭喜您获得").toString());
						}
						if (cr.getSignSeqDays() - 1 > 0 && (cr.getSignSeqDays() - 1) < (cr1.getSignSeqDays() - 1)) {
							cr1 = cr;
						}
					}
					// cr1为下一优惠券信息
					if (cr1.getSignSeqDays() != (Cycle+1)) {
						updateMap.put("get_next_coupon_need_days", (cr1.getSignSeqDays() - 1) + "");
						updateMap.put("next_activity_code", cr1.getActivityCode());
						apiUserSignResult.setGetNextCouponDays(cr1.getSignSeqDays() - 1);
						apiUserSignResult.setNextCouponName(activeCodeTransferToCounponInfo(cr1.getActivityCode()));
					}else {
						updateMap.put("get_next_coupon_need_days", (Cycle-1 + firstCoupon.getSignSeqDays() )+"");
						updateMap.put("next_activity_code", firstCoupon.getActivityCode());
						apiUserSignResult.setGetNextCouponDays(Cycle-1 + firstCoupon.getSignSeqDays() );
						apiUserSignResult.setNextCouponName(activeCodeTransferToCounponInfo(firstCoupon.getActivityCode()));
					}
				}

				// 将签到信息添加到签到表
				DbUp.upTable("mc_sign").dataInsert(updateMap);
				// 最后将签到信息添加到签到日志表
				DbUp.upTable("mc_sign_log").dataInsert(log);
			} else {// 用户有 签到记录
					// 获取用户今天签到标记
				String flag_sign_today = map.get("flag_sign_today").toString();
				// 如果今天已签到，不允许重复签到
				if ("1".equals(flag_sign_today)) {
					apiUserSignResult.setResultCode(0);
					apiUserSignResult.setResultMessage("今日已签到，请明天再来");
					return apiUserSignResult;
				} else {// 如果今天未签到，获取当前连续签到天数
					Object days = map.get("sign_seq_days");
					if (days != null) {
						Integer seqDays = (Integer) days;
						Integer newSeqDays = null;
						// 今天是否获得优惠券
						// 如果当前连续签到连续天数<Cycle，连续签到天数为昨天连续天数+1
						if (seqDays < Cycle) {
							newSeqDays = seqDays + 1;
							updateMap.put("sign_seq_days", newSeqDays + "");
							updateMap.put("flag_sign_today", "1");
							updateMap.put("sign_time", DateUtil.getNowTime());
							// 根据获取优惠券规则获取下一优惠券及需要天数
							if (couponRelatives != null) {// 有 优惠券可用
								// 下一优惠券对象
								CouponRelative cr1 = new CouponRelative();
								cr1.setSignSeqDays(Cycle+1);
								for (CouponRelative cr : couponRelatives) {
									if (cr.getSignSeqDays() - newSeqDays == 0) {
										bingoCouponRelative = cr;
										updateMap.put("sign_get_activity_code", cr.getActivityCode());
										log.put("get_activity_code", cr.getActivityCode());
									}
									if (cr.getSignSeqDays() - newSeqDays > 0 && (cr.getSignSeqDays()
											- newSeqDays) < (cr1.getSignSeqDays() - newSeqDays)) {
										cr1 = cr;
									}
								}
								// cr1为下一优惠券信息
								if (cr1.getSignSeqDays() != (Cycle+1)) {
									updateMap.put("get_next_coupon_need_days",
											(cr1.getSignSeqDays() - newSeqDays) + "");
									updateMap.put("next_activity_code", cr1.getActivityCode());
									apiUserSignResult.setGetNextCouponDays(cr1.getSignSeqDays() - newSeqDays);
									apiUserSignResult
											.setNextCouponName(activeCodeTransferToCounponInfo(cr1.getActivityCode()));
								}else {
									updateMap.put("get_next_coupon_need_days", (Cycle-newSeqDays + firstCoupon.getSignSeqDays() )+"");
									updateMap.put("next_activity_code", firstCoupon.getActivityCode());
									apiUserSignResult.setGetNextCouponDays(Cycle-newSeqDays + firstCoupon.getSignSeqDays() );
									apiUserSignResult.setNextCouponName(activeCodeTransferToCounponInfo(firstCoupon.getActivityCode()));
								}
							}
						} else {// 否则连续天数重新更新为1
							newSeqDays = 1;
							updateMap.put("sign_seq_days", newSeqDays + "");
							updateMap.put("flag_sign_today", "1");
							updateMap.put("sign_time", DateUtil.getNowTime());
							// 判断第一天签到有无优惠券发送
							if (couponRelatives != null) {// 有 优惠券可用
								// 下一优惠券对象
								CouponRelative cr1 = new CouponRelative();
								cr1.setSignSeqDays(Cycle+1);
								for (CouponRelative cr : couponRelatives) {
									if (cr.getSignSeqDays() - newSeqDays == 0) {
										bingoCouponRelative = cr;
										updateMap.put("sign_get_activity_code", cr.getActivityCode());
										log.put("get_activity_code", cr.getActivityCode());// get_activity_code
									}
									if (cr.getSignSeqDays() - newSeqDays > 0 && (cr.getSignSeqDays()
											- newSeqDays) < (cr1.getSignSeqDays() - newSeqDays)) {
										cr1 = cr;
									}
								}
								// cr1为下一优惠券信息
								if (cr1.getSignSeqDays() != (Cycle+1)) {
									updateMap.put("get_next_coupon_need_days",
											(cr1.getSignSeqDays() - newSeqDays) + "");
									updateMap.put("next_activity_code", cr1.getActivityCode());
									apiUserSignResult.setGetNextCouponDays(cr1.getSignSeqDays() - 1);
									apiUserSignResult
											.setNextCouponName(activeCodeTransferToCounponInfo(cr1.getActivityCode()));
								}else {
									updateMap.put("get_next_coupon_need_days", (Cycle - 1 + firstCoupon.getSignSeqDays() )+"");
									updateMap.put("next_activity_code", firstCoupon.getActivityCode());
									apiUserSignResult.setGetNextCouponDays(Cycle - 1 + firstCoupon.getSignSeqDays() );
									apiUserSignResult.setNextCouponName(activeCodeTransferToCounponInfo(firstCoupon.getActivityCode()));
								}
							}
						}
						// 返回结果连续签到天数
						apiUserSignResult.setSignSeqDays(newSeqDays);
						// 日志相关
						log.put("sign_seq_days", newSeqDays + "");
						// 更新数据库此用户的签到表状态
						DbUp.upTable("mc_sign").dataUpdate(updateMap,
								"sign_seq_days,flag_sign_today,sign_time,sign_get_activity_code,get_next_coupon_need_days,next_activity_code",
								"user_code");
						// 签到日志表添加签到记录
						DbUp.upTable("mc_sign_log").dataInsert(log);
					}
				}
			}
			// 根据签到天数调 发放对应天数优惠券接口，给当前登录用户发放对应优惠券
			if (bingoCouponRelative != null) {
				// 今日签到获得优惠券
				String activityCode = bingoCouponRelative.getActivityCode();
				String couponName = activeCodeTransferToCounponInfo(activityCode);
				couponName = new StringBuffer(couponName).insert(0, "恭喜您获得").toString();
				apiUserSignResult.setSignGetCouponName(couponName);
				String sql2 = "select login_name from mc_login_info where member_code = '" + member_code
						+ "'";
				List<Map<String, Object>> coupons = DbUp.upTable("mc_login_info").dataSqlList(sql2, null);
				if (coupons != null && coupons.size() > 0) {
					String telNum = (String) coupons.get(0).get("login_name");
					ApiForActivityCoupon afac = new ApiForActivityCoupon();
					ApiForActivityCouponInput input = new ApiForActivityCouponInput();
					input.setActivityCode(bingoCouponRelative.getActivityCode());
					input.setMobile(telNum);
					input.setValidateFlag("2");
					RootResultWeb process = afac.Process(input, mRequestMap);
					if(process.getResultCode()==939301319) {
						apiUserSignResult.setSignGetCouponName("很抱歉，优惠券发完了，请联系客服");
					}
				}
			}
		}
		return apiUserSignResult;
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
		}if(totalMoney!=0) {
			sb.append(totalMoney + "元福利");
		}
		if (countDiscounts != 0) {
			if(totalMoney!=0) {
				sb.append("," + countDiscounts + "张折扣券");
			}else {
				sb.append(countDiscounts + "张折扣券");
			}
		}
//		if(sb.toString().length()>0) {
//			sb.insert(0, "恭喜您获得");
//		}
		return sb.toString();
	}

}
