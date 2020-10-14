package com.cmall.familyhas.webfunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ProductMaybeLoveFuncSetTime extends RootFunc {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mWebResult = new MWebResult();
		MDataMap mSetTimeMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		try {
			String uid = mSetTimeMap.get("uid").trim();
			MDataMap mDataMap3 = DbUp.upTable("pc_product_maybelove").oneWhere(
					"zid,uid,product_code", "", "", "uid",uid);
			if (mDataMap3 != null) {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Calendar start_time = Calendar.getInstance();
				Calendar end_time = Calendar.getInstance();
				Calendar now_time = Calendar.getInstance();
				start_time.setTime(df.parse(mSetTimeMap.get("start_time")));
				end_time.setTime(df.parse(mSetTimeMap.get("end_time")));
				now_time.setTime(df.parse(df.format(new Date())));
				int result1 = end_time.compareTo(start_time);
				int result2 = end_time.compareTo(now_time);

				List<MDataMap> mDataMaps = new ArrayList<MDataMap>();
				Map<String, MDataMap> addMap = new HashMap<String, MDataMap>();
				mDataMaps = DbUp.upTable("pc_product_maybelove").queryAll(
						"uid,product_code,end_time,start_time", "", "", new MDataMap("product_code",mDataMap3.get("product_code")));
				for (MDataMap mDataMap4 : mDataMaps) {
					Calendar old_end_time = Calendar.getInstance();
					Calendar old_start_time = Calendar.getInstance();
					old_end_time.setTime(df.parse(mDataMap4.get("end_time")));// 已经存在商品的结束时间
					old_start_time.setTime(df.parse(mDataMap4.get("start_time")));// 已经存在商品的开始时间
					if (!mDataMap4.get("uid").equals(uid)) {
						if ((old_start_time.compareTo(start_time) < 0 && old_end_time.compareTo(start_time) > 0)
								|| (old_start_time.compareTo(end_time) < 0 && old_end_time.compareTo(end_time) > 0)
								||(start_time.compareTo(old_start_time)>0&&end_time.compareTo(old_end_time)<0)
								||(start_time.compareTo(old_start_time)<0&&end_time.compareTo(old_end_time)>0)
								||(result1 <= 0 || result2 <= 0)) {
								mWebResult.inErrorMessage(916421257);
						}else {
							continue;
						}
					}
				}
				if (mWebResult.upFlagTrue()) {
					// 获取当前登录人
					String create_user = UserFactory.INSTANCE.create()
							.getLoginName();
					mDataMap3.put("start_time",
							mSetTimeMap.get("start_time"));
					mDataMap3.put("end_time",
							mSetTimeMap.get("end_time"));
					mDataMap3
							.put("operate_time", new Date().toString());
					mDataMap3.put("operate_member", create_user);
					mDataMap3.put("position", mSetTimeMap.get("position"));
					/** 将惠家有信息更改pc_product_maybelove表中 */
					DbUp.upTable("pc_product_maybelove").update(
							mDataMap3);
				}else {
					mWebResult.inErrorMessage(916421257);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mWebResult;
	}

}
