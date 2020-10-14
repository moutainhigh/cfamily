package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.load.LoadCouponListForProduct;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改分销设置
 * @author sy
 *
 */
public class FuncEditAgentProduct extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		/*获取当前登录人*/
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		String uid = mDataMap.get("zw_f_uid");
		MDataMap map = DbUp.upTable("oc_activity_agent_product").one("uid",uid);
		
		if (mResult.upFlagTrue()) {
			
			mDataMap.put("zw_f_update_user", create_user);
			mDataMap.put("zw_f_update_time", createTime);
		}
		
		// 已经发过券则不允许修改金额
		if(DbUp.upTable("oc_coupon_info").count("activity_code", map.get("activity_code"), "coupon_type_code", map.get("coupon_type_code")) > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("优惠券已经被领取不能修改金额");
			return mResult;
		}
		
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
				MDataMap mDataMap2 = new MDataMap();
				mDataMap2.put("update_time", createTime);
				mDataMap2.put("updater", create_user);
				mDataMap2.put("money", mDataMap.get("zw_f_coupon_money"));
				mDataMap2.put("activity_code", map.get("activity_code"));
				mDataMap2.put("coupon_type_code", map.get("coupon_type_code"));
				DbUp.upTable("oc_coupon_type").dataUpdate(mDataMap2, "money,update_time,updater", "activity_code,coupon_type_code");
				new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
				new LoadCouponListForProduct().deleteInfoByCode(map.get("produt_code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
