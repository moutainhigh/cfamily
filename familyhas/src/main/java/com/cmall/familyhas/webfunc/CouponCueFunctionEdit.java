package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @author LHY
 * 2015年12月22日 下午2:31:53
 */
public class CouponCueFunctionEdit extends FuncEdit {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		if (mResult.upFlagTrue()) {
			String uid = mEditMaps.get("uid").trim();
			MDataMap map = DbUp.upTable("oc_coupon_cue").one("uid",uid);
			
			String title = mEditMaps.get("title");
			String description = mEditMaps.get("description");
			String pic = mEditMaps.get("pic");
			String is_use = mEditMaps.get("is_use");
			/*系统当前时间*/
			String create_time = DateUtil.getNowTime();
			/*获取当前登录人*/
			String creator = UserFactory.INSTANCE.create().getLoginName();
			/*获取当前app*/
			String appCode = UserFactory.INSTANCE.create().getManageCode();
			if(title.length()>12){
				mResult.inErrorMessage(916422121);
				return mResult;
			}
			map.put("title", title);
			map.put("description", description);
			map.put("pic", pic);
			map.put("is_use", is_use);
			map.put("editor", creator);
			map.put("edit_time", create_time);
			map.put("app_code", appCode);
			
			DbUp.upTable("oc_coupon_cue").update(map);
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

}
