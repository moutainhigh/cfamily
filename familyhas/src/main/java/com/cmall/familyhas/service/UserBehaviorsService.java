package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.ApiForActivityCoupon;
import com.cmall.familyhas.api.input.ApiForActivityCouponInput;
import com.cmall.familyhas.api.result.ApiListenerUserBehaviorsResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 根据用户行为积累来判断是否进行系统发券
 * 
 * @author zb
 *
 */
public class UserBehaviorsService {

	public ApiListenerUserBehaviorsResult recordUsersBeahviors(String behaviorType, String userCode, String loginName,
			String manageCode) {

		ApiListenerUserBehaviorsResult result = new ApiListenerUserBehaviorsResult();

		String userBehaviorType = behaviorType;

		String member_code = userCode;
		if (StringUtils.isBlank(member_code)) {
			return result;
		}
		String currentTime = FormatHelper.upDateTime();
		if (StringUtils.isNotBlank(member_code)) {
			if ("9".equals(userBehaviorType)) {
				// 应用启动
				MDataMap userBehaviorsRecordsMap = DbUp.upTable("mc_member_behaviors").one("user_code", member_code,
						"user_behavior_type", "9");
				MDataMap behaviroConfigMap = DbUp.upTable("oc_coupon_relative_behavior").one("relative_type", "9");

				if (behaviroConfigMap != null) {
					// 已有配置活动
					// 查看是达到发券条件
					String start_up_days = behaviroConfigMap.get("start_up_days");

					if (userBehaviorsRecordsMap == null) {
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("uid", WebHelper.upUuid());
						mDataMap.put("user_code", member_code);
						mDataMap.put("user_behavior_type", "9");
						mDataMap.put("user_seq_days", "1");
						mDataMap.put("user_operate_frequency", "1");
						mDataMap.put("flag", "0");
						mDataMap.put("create_time", currentTime);
						mDataMap.put("update_time", currentTime);
						DbUp.upTable("mc_member_behaviors").dataInsert(mDataMap);

						if ("1".equals(start_up_days)) {
							// 达到的话触发发券，并更新连续时间为0,今日操作频次为0
							/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
									CouponConst.start_up_coupon,
									new MDataMap("mobile", loginName, "manage_code", manageCode));*/
							//改为实时发放
							distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
							mDataMap.put("user_seq_days", "0");
							mDataMap.put("user_operate_frequency", "0");
							mDataMap.put("flag", "1");
							DbUp.upTable("mc_member_behaviors").dataUpdate(mDataMap,
									"user_seq_days,update_time,user_operate_frequency,flag",
									"user_code,user_behavior_type");
						}

					} else {
						// 用户已有行为记录
						String user_seq_days = userBehaviorsRecordsMap.get("user_seq_days");
						String update_time = userBehaviorsRecordsMap.get("update_time");
						String user_operate_frequency = userBehaviorsRecordsMap.get("user_operate_frequency");
						String flag = userBehaviorsRecordsMap.get("flag");

						// 最近行为时间
						String lastOperatDay = StringUtils.substring(update_time, 0, 10);
						String yesterdayStr = StringUtils.substring(DateUtil.addDateHour(currentTime, -24), 0, 10);
						if (yesterdayStr.equals(lastOperatDay)) {
							// 昨日有行为
							user_seq_days = (Integer.parseInt(user_seq_days) + 1) + "";
							if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)) {
								// 达到活动配置天数，发券，更新记录
								/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
										CouponConst.start_up_coupon,
										new MDataMap("mobile", loginName, "manage_code", manageCode));*/
								//改为实时发放
								distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
								userBehaviorsRecordsMap.put("user_seq_days", "0");
								userBehaviorsRecordsMap.put("update_time", currentTime);
								userBehaviorsRecordsMap.put("user_operate_frequency", "0");
								userBehaviorsRecordsMap.put("flag", "1");
								DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
										"user_seq_days,update_time,user_operate_frequency,flag",
										"user_code,user_behavior_type");
							} else {
								// 未达到活动配置天数，更新记录
								userBehaviorsRecordsMap.put("user_seq_days", user_seq_days + "");
								userBehaviorsRecordsMap.put("update_time", currentTime);
								userBehaviorsRecordsMap.put("user_operate_frequency", "1");
								userBehaviorsRecordsMap.put("flag", "0");
								DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
										"user_seq_days,update_time,user_operate_frequency,flag",
										"user_code,user_behavior_type");

							}
						} else if (StringUtils.substring(currentTime, 0, 10).equals(lastOperatDay)) {
							// 今日有操作，并且今日未有发券触发则做更新操作
							if ("0".equals(flag)) {
								user_operate_frequency = (Integer.parseInt(user_operate_frequency) + 1) + "";
								user_seq_days="1";
								if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)) {
									// 达到活动配置天数，发券，更新记录
									/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
											CouponConst.start_up_coupon,
											new MDataMap("mobile", loginName, "manage_code", manageCode));*/
									//改为实时发放
									distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
									userBehaviorsRecordsMap.put("user_seq_days", "0");
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("user_operate_frequency", "0");
									userBehaviorsRecordsMap.put("flag", "1");
									DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
											"user_seq_days,update_time,user_operate_frequency,flag",
											"user_code,user_behavior_type");
								}else{
									userBehaviorsRecordsMap.put("user_operate_frequency", user_operate_frequency);
									userBehaviorsRecordsMap.put("update_time", currentTime);
									DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
											"update_time,user_operate_frequency", "user_code,user_behavior_type");	
									}
							}

						} else {
							// 昨日无行为，重置记录数据 连续天数1 更新时间今天
							userBehaviorsRecordsMap.put("user_seq_days", "1");
							userBehaviorsRecordsMap.put("update_time", currentTime);
							userBehaviorsRecordsMap.put("user_operate_frequency", "1");
							userBehaviorsRecordsMap.put("flag", "0");
							if ("1".equals(start_up_days)) {
								// 达到的话触发发券，并更新连续时间为0,今日操作频次为0
								/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
										CouponConst.start_up_coupon,
										new MDataMap("mobile", loginName, "manage_code", manageCode));*/
								//改为实时发放
								distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
								userBehaviorsRecordsMap.put("user_seq_days", "0");
								userBehaviorsRecordsMap.put("user_operate_frequency", "0");
								userBehaviorsRecordsMap.put("flag", "1");
								userBehaviorsRecordsMap.put("update_time", currentTime);
							}
							DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
									"user_seq_days,update_time,user_operate_frequency,flag",
									"user_code,user_behavior_type");
						}

					}
				}
			} else if ("10".equals(userBehaviorType)) {
				// 应用启动
				MDataMap userBehaviorsRecordsMap = DbUp.upTable("mc_member_behaviors").one("user_code", member_code,
						"user_behavior_type", "10");
				MDataMap behaviroConfigMap = DbUp.upTable("oc_coupon_relative_behavior").one("relative_type", "10");

				if (behaviroConfigMap != null) {
					// 已有配置活动
					// 查看是达到发券条件
					String start_up_days = behaviroConfigMap.get("start_up_days");
					String add_shop_car = behaviroConfigMap.get("add_shop_car");
					if (userBehaviorsRecordsMap == null) {
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("uid", WebHelper.upUuid());
						mDataMap.put("user_code", member_code);
						mDataMap.put("user_behavior_type", "10");
						mDataMap.put("user_operate_frequency", "1");
						mDataMap.put("flag", "0");
						if (Integer.parseInt(start_up_days)==1&&Integer.parseInt(add_shop_car) == 0) {
							mDataMap.put("user_seq_days", "1");
						} else {
							mDataMap.put("user_seq_days", "0");
						}
						mDataMap.put("create_time", currentTime);
						mDataMap.put("update_time", currentTime);
						DbUp.upTable("mc_member_behaviors").dataInsert(mDataMap);

						if ("1".equals(start_up_days) && "0".equals(add_shop_car)) {
							// 达到的话触发发券，并更新连续时间为0,今日操作频次为0
							/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
									CouponConst.add_shop_car_coupon,
									new MDataMap("mobile", loginName, "manage_code", manageCode));*/
							//改为实时发放
							distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
							mDataMap.put("user_seq_days", "0");
							mDataMap.put("user_operate_frequency", "0");
							mDataMap.put("flag", "1");
							DbUp.upTable("mc_member_behaviors").dataUpdate(mDataMap,
									"user_seq_days,update_time,user_operate_frequency,flag",
									"user_code,user_behavior_type");
						}

					} else {
						// 用户已有行为记录
						String user_seq_days = userBehaviorsRecordsMap.get("user_seq_days");
						String update_time = userBehaviorsRecordsMap.get("update_time");
						String user_operate_frequency = userBehaviorsRecordsMap.get("user_operate_frequency");
						String flag = userBehaviorsRecordsMap.get("flag");
						// 最近行为时间
						String lastOperatDay = StringUtils.substring(update_time, 0, 10);
						String yesterdayStr = StringUtils.substring(DateUtil.addDateHour(currentTime, -24), 0, 10);
						if (yesterdayStr.equals(lastOperatDay)) {
							
							if ("0".equals(flag)) {
								
								
								//先判断昨天的行为记录是否为连续的
								if( Integer.parseInt(user_operate_frequency) > Integer.parseInt(add_shop_car)) {
									// 昨日有操作行为
									user_operate_frequency = "0";
									if (Integer.parseInt(user_operate_frequency) == Integer.parseInt(add_shop_car)) {
										// 昨日操作满足行为累计条件
										user_seq_days = (Integer.parseInt(user_seq_days) + 1) + "";
									} 
									user_operate_frequency="1";
									if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)
											&& Integer.parseInt(user_operate_frequency) > Integer.parseInt(add_shop_car)) {
										// 达到活动配置
										/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
												CouponConst.add_shop_car_coupon,
												new MDataMap("mobile", loginName, "manage_code", manageCode));*/
										//改为实时发放
										distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
										userBehaviorsRecordsMap.put("user_seq_days", "0");
										userBehaviorsRecordsMap.put("update_time", currentTime);
										userBehaviorsRecordsMap.put("user_operate_frequency", "0");
										userBehaviorsRecordsMap.put("flag", "1");
									} else {
										// 未达到活动配置天数，更新记录
										userBehaviorsRecordsMap.put("user_seq_days", user_seq_days);
										userBehaviorsRecordsMap.put("update_time", currentTime);
										userBehaviorsRecordsMap.put("user_operate_frequency", user_operate_frequency);
										userBehaviorsRecordsMap.put("flag", "0");
									}
								}else {
									//昨天未连续,今日重新计算
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("user_operate_frequency", "1");
									if (Integer.parseInt(add_shop_car) == 0) {
										userBehaviorsRecordsMap.put("user_seq_days", "1");
										user_seq_days = "1";
									} else {
										userBehaviorsRecordsMap.put("user_seq_days", "0");
										user_seq_days = "0";
									}
									userBehaviorsRecordsMap.put("flag", "0");

									if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)
											&& Integer.parseInt("1") > Integer.parseInt(add_shop_car)) {
										// 达到活动配置
										/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
												CouponConst.add_shop_car_coupon,
												new MDataMap("mobile", loginName, "manage_code", manageCode));*/
										//改为实时发放
										distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
										userBehaviorsRecordsMap.put("user_seq_days", "0");
										userBehaviorsRecordsMap.put("update_time", currentTime);
										userBehaviorsRecordsMap.put("user_operate_frequency", "0");
										userBehaviorsRecordsMap.put("flag", "1");
									} else {
										// 未达到活动配置天数，更新记录
										userBehaviorsRecordsMap.put("user_seq_days", user_seq_days);
										userBehaviorsRecordsMap.put("update_time", currentTime);
										userBehaviorsRecordsMap.put("flag", "0");
									}
									
								}
							
							} else {
								// 昨日有发券触发,则行为操作重置
								userBehaviorsRecordsMap.put("update_time", currentTime);
								userBehaviorsRecordsMap.put("user_operate_frequency", "1");
								if (Integer.parseInt(add_shop_car) == 0) {
									userBehaviorsRecordsMap.put("user_seq_days", "1");
									user_seq_days = "1";
								} else {
									userBehaviorsRecordsMap.put("user_seq_days", "0");
									user_seq_days = "0";
								}
								userBehaviorsRecordsMap.put("flag", "0");

								if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)
										&& Integer.parseInt("1") > Integer.parseInt(add_shop_car)) {
									// 达到活动配置
									/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
											CouponConst.add_shop_car_coupon,
											new MDataMap("mobile", loginName, "manage_code", manageCode));*/
									//改为实时发放
									distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
									userBehaviorsRecordsMap.put("user_seq_days", "0");
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("user_operate_frequency", "0");
									userBehaviorsRecordsMap.put("flag", "1");
								} else {
									// 未达到活动配置天数，更新记录
									userBehaviorsRecordsMap.put("user_seq_days", user_seq_days);
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("flag", "0");
								}

							}
							DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
									"user_seq_days,update_time,user_operate_frequency,flag",
									"user_code,user_behavior_type");

						} else if (StringUtils.substring(currentTime, 0, 10).equals(lastOperatDay)) {
							if ("0".equals(flag)) {
								// 如果今日未触发发券，则继续进行行为记录	
								if (Integer.parseInt(user_operate_frequency) == Integer.parseInt(add_shop_car)) {
									user_seq_days = (Integer.parseInt(user_seq_days) + 1) + "";
								}
								user_operate_frequency = (Integer.parseInt(user_operate_frequency) + 1) + "";
								userBehaviorsRecordsMap.put("update_time", currentTime);

								if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)
										&& Integer.parseInt(user_operate_frequency) > Integer.parseInt(add_shop_car)) {
									// 达到活动配置
									/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
											CouponConst.add_shop_car_coupon,
											new MDataMap("mobile", loginName, "manage_code", manageCode));*/
									//改为实时发放
									distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
									userBehaviorsRecordsMap.put("user_seq_days", "0");
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("user_operate_frequency", "0");
									userBehaviorsRecordsMap.put("flag", "1");
								} else {
									// 未达到活动配置
									userBehaviorsRecordsMap.put("user_seq_days", user_seq_days + "");
									userBehaviorsRecordsMap.put("update_time", currentTime);
									userBehaviorsRecordsMap.put("user_operate_frequency", user_operate_frequency);
									userBehaviorsRecordsMap.put("flag", "0");
								}

								DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
										"user_seq_days,update_time,user_operate_frequency,flag",
										"user_code,user_behavior_type");

							}

						} else {
							// 昨日无行为，重置记录数据
							user_operate_frequency = "1";
							if (Integer.parseInt(add_shop_car) == 0) {
								user_seq_days = "1";
							} else {
								user_seq_days = "0";
							}

							if (Integer.parseInt(user_seq_days) >= Integer.parseInt(start_up_days)
									&& Integer.parseInt(user_operate_frequency) > Integer.parseInt(add_shop_car)) {
								// 达到活动配置
								/*JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
										CouponConst.add_shop_car_coupon,
										new MDataMap("mobile", loginName, "manage_code", manageCode));*/
								//改为实时发放
								distributeCoupons(behaviroConfigMap.get("activity_code"),loginName);
								userBehaviorsRecordsMap.put("user_seq_days", "0");
								userBehaviorsRecordsMap.put("update_time", currentTime);
								userBehaviorsRecordsMap.put("user_operate_frequency", "0");
								userBehaviorsRecordsMap.put("flag", "1");
							} else {
								// 未达到活动配置天数，更新记录
								userBehaviorsRecordsMap.put("user_seq_days", user_seq_days + "");
								userBehaviorsRecordsMap.put("update_time", currentTime);
								userBehaviorsRecordsMap.put("user_operate_frequency", user_operate_frequency);
								userBehaviorsRecordsMap.put("flag", "0");
							}

							DbUp.upTable("mc_member_behaviors").dataUpdate(userBehaviorsRecordsMap,
									"user_seq_days,update_time,user_operate_frequency,flag",
									"user_code,user_behavior_type");
						}

					}
				}
			} else {
				result.setResultCode(0);
				result.setResultMessage("错误的用户行为监听类型");
			}
			
			//用戶行為日誌记录
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("uid",WebHelper.upUuid() );
			mDataMap.put("user_code",userCode );
			mDataMap.put("user_behavior_type",behaviorType );
			mDataMap.put("create_time", currentTime);
	        DbUp.upTable("lc_mc_member_behaviors_log").dataInsert(mDataMap);
			
		}
		return result;

	}

	public boolean distributeCoupons(String activityCode, String phone) {

		ApiForActivityCoupon afac = new ApiForActivityCoupon();
		ApiForActivityCouponInput input = new ApiForActivityCouponInput();
		input.setActivityCode(activityCode);
		input.setMobile(phone);
		input.setValidateFlag("2");
		RootResultWeb process = afac.Process(input, new MDataMap());
		if (process.getResultCode() == 939301319) {
			System.out.println("'启动应用|添加购物车'触发发券优惠券发放完~");
			return false;
		}
		return true;
	}

}
