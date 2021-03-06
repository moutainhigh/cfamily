package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class BrandPreferenceContentFuncAdd extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String locationCode = mDataMap.get("zw_f_brand_location");
		int count = DbUp.upTable("fh_brand_cotent_preference").count(
				"brand_location", locationCode, "info_code", mDataMap.get("zw_f_info_code"));//判断某种位置的广告是否已经存在
		if(count > 0){
			String location = "";
			if("1".equals(locationCode)){
				location = "头部";
			}else{
				location = "尾部";
			}
			mResult.inErrorMessage(916401233, location);
			return mResult;
		}
		/* 系统当前时间 */
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_create_time", createTime);
		mDataMap.put("zw_f_update_user", create_user);
		mDataMap.put("zw_f_update_time", createTime);
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
