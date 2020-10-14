package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @author LHY
 * 2015年12月22日 下午2:31:53
 */
public class CouponCueFunctionAdd extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		if (mResult.upFlagTrue()) {
			MDataMap map = new MDataMap();
			String title = mAddMaps.get("title");
			String description = mAddMaps.get("description");
			String pic = mAddMaps.get("pic");
			String is_use = mAddMaps.get("is_use");
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
			map.put("creator", creator);
			map.put("create_time", create_time);
			map.put("app_code", appCode);
			
			DbUp.upTable("oc_coupon_cue").dataInsert(map);
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

}
