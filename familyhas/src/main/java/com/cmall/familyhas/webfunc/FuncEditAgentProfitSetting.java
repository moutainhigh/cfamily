package com.cmall.familyhas.webfunc;

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
public class FuncEditAgentProfitSetting extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mDataMap1 = new MDataMap();
		String coupon_rate = Double.parseDouble(mDataMap.get("zw_f_coupon_rate"))/100+"";
		String agent_rate = Double.parseDouble(mDataMap.get("zw_f_agent_rate"))/100+"";
		String fans_rate = Double.parseDouble(mDataMap.get("zw_f_fans_rate"))/100+"";
		String company_rate = Double.parseDouble(mDataMap.get("zw_f_company_rate"))/100+"";
		String uid = mDataMap.get("zw_f_uid");
		
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			
			mDataMap1.put("zw_f_etr_id", create_user);
			mDataMap1.put("zw_f_coupon_rate", coupon_rate);
			mDataMap1.put("zw_f_agent_rate", agent_rate);
			mDataMap1.put("zw_f_fans_rate", fans_rate);
			mDataMap1.put("zw_f_company_rate", company_rate);
			mDataMap1.put("zw_f_uid", uid);
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap1);
				MDataMap mDataMap2 = new MDataMap();
				mDataMap2.put("etr_id", UserFactory.INSTANCE.create().getLoginName());
				mDataMap2.put("coupon_rate", coupon_rate);
				mDataMap2.put("agent_rate", agent_rate);
				mDataMap2.put("fans_rate", fans_rate);
				mDataMap2.put("company_rate", company_rate);
				DbUp.upTable("lc_agent_profit_setting_log").dataInsert(mDataMap2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
