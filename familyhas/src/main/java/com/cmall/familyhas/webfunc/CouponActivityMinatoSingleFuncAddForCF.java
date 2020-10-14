package com.cmall.familyhas.webfunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 优惠卷过期提醒添加
 * @author wei.che
 * @Date 2015-12-17
 */
public class CouponActivityMinatoSingleFuncAddForCF extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String activity_code = mAddMaps.get("activity_codes_select").trim();
		if (StringUtils.isEmpty(activity_code)) {
			mResult.inErrorMessage(916423006);//未选择活动，请选择
		}else if (StringUtils.isBlank(mAddMaps.get("send_time"))){
			mResult.inErrorMessage(916423008);//提醒时间不能为空
		}else{	
			//做时间的比较
			//MDataMap mSetTimeMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar send_time = Calendar.getInstance();
			send_time.setTime(df.parse(mSetTimeMap.get("send_time")));*/
			
			//活动名称
			String activity_names = "";
			//活动编号
			String activity_codes = "";
			//查询选择的活动
			String datawhere = " SELECT * from oc_activity where activity_code in ("+subStringToQueryForIn(activity_code)+") ";
			List<Map<String, Object>> activityList = DbUp.upTable("oc_activity").dataSqlList(datawhere, null);
			
			for(Map<String, Object> map : activityList){
				activity_names += map.get("activity_name")+",";
				activity_codes += map.get("activity_code")+",";
			}
			
			// 创建时间为当年系统时间
			SimpleDateFormat create_time = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			mAddMaps.put("create_time", create_time.format(new Date()));
			/* 来源系统 */
			String seller_code = UserFactory.INSTANCE.create().getManageCode();
			mAddMaps.put("seller_code", seller_code);
			mAddMaps.put("uid",
					UUID.randomUUID().toString().replace("-", ""));
			mAddMaps.remove("activity_codes_select");
			mAddMaps.put("activity_names", activity_names.substring(0, activity_names.length()-1));
			mAddMaps.put("activity_codes", activity_codes.substring(0, activity_codes.length()-1));
			if (mResult.upFlagTrue()) {
				DbUp.upTable("oc_coupon_remind_setting").dataInsert(
						mAddMaps);
			}
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}
	
	
	/**
	 * 将字符串转换为in方式查询中的条件
	 * @param chaString
	 * @return
	 */
	private String subStringToQueryForIn(String chaString){
		StringBuffer returnStr = new StringBuffer("");
		String[] strs = chaString.split(",");
		for(int i = 0;i<strs.length;i++){
			if(i!=strs.length-1){
				returnStr.append("'"+strs[i]+"',");
			}else{
				returnStr.append("'"+strs[i]+"'");
			}
		}
		return returnStr.toString();
	}

	

}
