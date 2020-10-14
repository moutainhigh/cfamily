package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiSignRemindInput;
import com.cmall.familyhas.api.result.ApiSignRemindResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.CouponRelative;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiForSignRemind extends RootApiForToken<ApiSignRemindResult, ApiSignRemindInput>{

	@Override
	public ApiSignRemindResult Process(ApiSignRemindInput inputParam, MDataMap mRequestMap) {

		ApiSignRemindResult apiSignRemindResult = new ApiSignRemindResult();
		// 获取用户编号
		String member_code = getUserCode();
		//获取 提醒标记 1为提醒，0为不提醒
		String remindFlag = inputParam.getRemindFlag();
		//将用户提醒状态存到redis库
		try {
//			KvFactory kvFactory = new KvFactory("xd-" + EUserSign.signRemind.toString() + "-");
//			kvFactory.set(member_code, remindFlag);
//			sign_remind_flag
			//查看数据库签到表是否有此用户签到信息,如果有更新签到提醒状态，如果没有插入一条数据
			String sql = "select * from mc_sign where user_code =  '" + member_code + "'";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_sign").dataSqlList(sql, null);
			MDataMap updateMap = new MDataMap();
			updateMap.put("sign_remind_flag", remindFlag);
			updateMap.put("user_code", member_code);
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				DbUp.upTable("mc_sign").dataUpdate(updateMap,"sign_remind_flag","user_code");
			}else {
				updateMap.put("get_next_coupon_need_days", "0");
				updateMap.put("next_activity_code", "");
				String nowTime = DateUtil.getNowTime();
				String sql1 = "SELECT	a.* FROM	oc_coupon_relative_expand a JOIN oc_activity b ON a.activity_code = b.activity_code WHERE b.flag = 1  and b.begin_time < '"+nowTime+"' AND b.end_time > '"+nowTime+"'";
				List<Map<String, Object>> couponList = DbUp.upTable("oc_coupon_relative_expand").dataSqlList(sql1, null);
				CouponRelative firstCoupon = new CouponRelative();
				firstCoupon.setSignSeqDays(22);
				if (couponList != null) {
					for (Map<String, Object> ma1 : couponList) {
						CouponRelative couponRelative = new CouponRelative();
						couponRelative.setActivityCode(ma1.get("activity_code").toString());
						couponRelative.setSignSeqDays((Integer) ma1.get("sign_days_get_coupon"));
						if(couponRelative.getSignSeqDays()<firstCoupon.getSignSeqDays()) {
							firstCoupon = couponRelative;
						}
					}
				}
				if(firstCoupon.getSignSeqDays()!=22) {
					updateMap.put("get_next_coupon_need_days", firstCoupon.getSignSeqDays()+"");
					updateMap.put("next_activity_code", firstCoupon.getActivityCode());
				}
				DbUp.upTable("mc_sign").dataInsert(updateMap);
			}
			apiSignRemindResult.setToggleResult("切换成功");
		}catch(Exception e) {
			apiSignRemindResult.setToggleResult("切换失败，请联系管理员");
		}
		return apiSignRemindResult;
	}

}
