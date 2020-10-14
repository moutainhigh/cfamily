package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券类型
 * @author ligj
 * time 2015-06-03 14:21:00
 *
 */
public class FuncAddCouponTypeNew3 extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		String totalMoney = mDataMap.get("zw_f_total_money");
//		String produceFype =  mDataMap.get("zw_f_produce_type");		//生成类型
//		String cdkey =  mDataMap.get("zw_f_cdkey");						//优惠码
		
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		
		//有效类型为范围时需验证开始结束时间
		if ("4497471600080002".equals(mDataMap.get("zw_f_valid_type"))) {
			if (endTime.compareTo(startTime) <= 0) {
				//开始时间必须小于结束时间!
				mResult.inErrorMessage(916401201);
				return mResult;
			}else if (endTime.compareTo(createTime) <= 0) {
//			当前时间必须小于结束时间!
				mResult.inErrorMessage(916401214);
				return mResult;
			}
			//有效类型为范围时天数字段设置为0
			mDataMap.put("zw_f_valid_day", "0");
		}else{
			//有效类型为天数时开始结束时间设置为空
			mDataMap.put("zw_f_start_time", "");
			mDataMap.put("zw_f_end_time", "");
			
			String validDay = mDataMap.get("zw_f_valid_day");
			try {
				if (Integer.parseInt(validDay) <= 0) {
					mResult.inErrorMessage(916421244);
					return mResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(916421245);
				return mResult;
			}
			
		}
		//跳转类型为商品详情时，跳转链接字段设置为空
		if ("4497471600280001".equals(mDataMap.get("zw_f_action_type"))) {
			mDataMap.put("zw_f_action_value", "");
		}
		//优惠券类型为折扣券时，将折扣券的值赋给优惠金额，两者公用同一字段
		if ("449748120002".equals(mDataMap.get("zw_f_money_type"))) {
			mDataMap.put("zw_f_money", mDataMap.get("zw_f_discount"));			
		}
		
		MDataMap activityMap = DbUp.upTable("oc_activity").one("activity_code", mDataMap.get("zw_f_activity_code"));
		// 金额券
		if("449748120001".equals(mDataMap.get("zw_f_money_type"))) {
			// 总发放份数乘以单张面额
			totalMoney = new BigDecimal(activityMap.get("provide_num")).multiply(new BigDecimal(mDataMap.get("zw_f_money"))).intValue()+"";
		} else {
			// 其他默认取发放份数
			totalMoney = activityMap.get("provide_num");
		}
		
		mDataMap.remove("zw_f_discount");
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_creater", create_user);
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updater", create_user);
			mDataMap.put("zw_f_coupon_type_code", WebHelper.upCode("CT"));
			mDataMap.put("zw_f_surplus_money", totalMoney);
			mDataMap.put("zw_f_total_money", totalMoney);
			mDataMap.put("zw_f_privide_money", "0");			//添加时已发放金额默认为0
			mDataMap.put("zw_f_manage_code", UserFactory.INSTANCE.create().getManageCode());
		}
		//类型为优惠码时。优惠码不能重复   edit 2015-05-28
//		if ("4497471600040002".equals(produceFype)) {
//			int checkCDKEYRepeat = DbUp.upTable("oc_coupon_type").count("cdkey",cdkey);
//			if (checkCDKEYRepeat > 0) {
//				//已经存在此优惠码，请重新设置！
//				mResult.inErrorMessage(916401228);
//				return mResult;
//			}
//		}
		
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
