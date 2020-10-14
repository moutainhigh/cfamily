package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class BrandPreferenceFuncAdd extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		/*内容编号*/
		String info_code = WebHelper.upCode("HJY");
		
		/*状态*/
		String flag_show = mAddMaps.get("flag_show").replace("'", "").trim();
		
		/*所属分类*/
		String info_category = mAddMaps.get("info_category").replace("'", "").trim();
		
		/*系统当前时间*/
		String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		
		/*获取当前登录人*/
		
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		
		/*所属app*/
		String app_code = bConfig("familyhas.app_code");
		
		mAddMaps.put("create_time", create_time);
		
		mAddMaps.put("info_code", info_code);
		
		mAddMaps.put("info_category", info_category);
		String startTime = mAddMaps.get("up_time").trim();
		String endTime = mAddMaps.get("down_time").trim();
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		if (endTime.compareTo(startTime) <= 0) {
			//开始时间必须小于结束时间!
			mResult.inErrorMessage(916401201);
			return mResult;
		}else if (endTime.compareTo(createTime) <= 0) {
			//当前时间必须小于结束时间!
			mResult.inErrorMessage(916401214);
			return mResult;
		}
		String shareFlag = mAddMaps.get("share_flag");
		if("449747110001".equals(shareFlag)){//不分享
			mAddMaps.put("share_img_url","");
			mAddMaps.put("share_title","");
			mAddMaps.put("share_content","");
		}
		String info_title = mAddMaps.get("info_title").replace("'", "").trim();
		if(null == info_title || "".equals(info_title)){
			mAddMaps.remove("info_title");
		}
		String sort_num = mAddMaps.get("sort_num").replace("'", "").trim();
		
		if(null == sort_num || "".equals(sort_num)){
			mAddMaps.remove("sort_num");
		}
		
		String photos = mAddMaps.get("photos").trim();
		if(null == photos || "".equals(photos)){
			mAddMaps.remove("photos");
		}
//		String up_time = mAddMaps.get("up_time").trim();
		if(null == startTime || "".equals(startTime)){
			mAddMaps.remove("up_time");
		}
//		String down_time = mAddMaps.get("down_time").trim();
		if(null == endTime || "".equals(endTime)){
			mAddMaps.remove("down_time");
		}
		mAddMaps.put("create_member", create_user);
		
		mAddMaps.put("manage_code", app_code);
		try{
			if (mResult.upFlagTrue()) {
				/**将惠家有信息插入fh_brand_preference表中*/
				DbUp.upTable("fh_brand_preference").dataInsert(mAddMaps);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		
		return mResult;
	}

}
