package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForActivityCouponInput;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 领取活动优惠券(只支持系统发放的活动)
 * @author 张海生
 *
 */
public class ApiForActivityCoupon 
			extends RootApiForManage<RootResultWeb, ApiForActivityCouponInput>{
	public RootResultWeb Process(ApiForActivityCouponInput input,MDataMap mRequestMap){
		RootResultWeb result=new RootResultWeb();
		String activityCode = input.getActivityCode();
		MDataMap acMap = DbUp.upTable("oc_activity").oneWhere("provide_type", "", "", "activity_code",activityCode);
		//wap系统要求发放形式为人工发放，用活动编号和用户手机号来领券.
		if(acMap != null && !"4497471600060002".equals(acMap.get("provide_type"))) {
			result.inErrorMessage(916423009);//只支持系统发放类型的优惠券活动
			return result;
		}
		CouponsService couponsService = new CouponsService();
		result = couponsService.distributeCoupons(input.getActivityCode(), input.getMobile(), input.getValidateFlag());//领取优惠券
		return result;
	}
	

}
