package com.cmall.familyhas.webfunc;

import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除草稿箱
 * @author LHY
 * 2016年4月29日 下午2:02:16
 */
public class MerchantDeleteDrafts extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		//获取当前操作人
		MUserInfo userInfo = null;
		String userCode = "";
		if (UserFactory.INSTANCE != null) {
			try {
				userInfo = UserFactory.INSTANCE.create();
			} catch (Exception e) {
			}
			if (userInfo != null) {
				userCode = userInfo.getUserCode();
			}
		}
		try {
			if (mResult.upFlagTrue()) {
				mDataMap.put("uid",mDelMaps.get("uid"));
				mDataMap.put("flag_del", "4497478100040002");
				mDataMap.put("update_time", DateUtil.getSysDateTimeString());
				mDataMap.put("update_user", userCode);
				DbUp.upTable("uc_seller_info_extend_draft").dataUpdate(mDataMap,"flag_del,update_time,update_user","uid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResult;
	}
}
