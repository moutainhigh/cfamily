package com.cmall.familyhas.webfunc;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 增加导航类型
 * @author liqt
 *
 */
public class NavigationSelect extends RootFunc{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		MDataMap maddMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			//当前登录人
			String operateMember = UserFactory.INSTANCE.create().getLoginName();
			//当前时间
			String operateTime = DateUtil.getNowTime();
			String startTime = maddMap.get("start_time");
			String endTime = maddMap.get("end_time");
			if(!DateUtil.getTimefag(endTime, startTime)){
				mResult.inErrorMessage(916401201);// 开始时间必须小于结束时间
				return mResult;
			}else if(!DateUtil.getTimefag(endTime, operateTime)){
				mResult.inErrorMessage(916401214);// 当前时间必须小于结束时间
				return mResult;
			}
			
			MDataMap minserMap = new MDataMap();
			minserMap.put("navigation_type", maddMap.get("navigation_type"));
			minserMap.put("before_pic", maddMap.get("before_pic"));
			minserMap.put("after_pic", maddMap.get("after_pic"));
			minserMap.put("start_time", startTime);
			minserMap.put("end_time", endTime);
			minserMap.put("operate_member", operateMember);
			minserMap.put("operate_time", operateTime);
			minserMap.put("type_name", maddMap.get("type_name"));
			minserMap.put("before_fontcolor", maddMap.get("before_fontcolor"));
			minserMap.put("after_fontcolor", maddMap.get("after_fontcolor"));
			minserMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
			
			minserMap.put("showmore_linktype", StringUtils.trimToEmpty(maddMap.get("showmore_linktype")));
			minserMap.put("showmore_linkvalue", StringUtils.trimToEmpty(maddMap.get("showmore_linkvalue")));
			
			minserMap.put("min_program_id", StringUtils.trimToEmpty(maddMap.get("min_program_id")));
			minserMap.put("is_show", maddMap.get("is_show"));
			
			if("4497467900040001".equals(maddMap.get("navigation_type"))){
				minserMap.put("firstpage_version", KvHelper.upCode("stVersion"));
				
			}else if ("4497467900040002".equals(maddMap.get("navigation_type"))) {
				minserMap.put("assortment_version", KvHelper.upCode("stVersion"));
				
			}else if ("4497467900040003".equals(maddMap.get("navigation_type"))) {
				minserMap.put("shoppingcart_version", KvHelper.upCode("stVersion"));
				
			}else if("4497467900040004".equals(maddMap.get("navigation_type"))){
				minserMap.put("mine_version", KvHelper.upCode("stVersion"));
			
			}else if("4497467900040005".equals(maddMap.get("navigation_type"))){
				minserMap.put("background_version", KvHelper.upCode("stVersion"));
			}else if("4497467900040006".equals(maddMap.get("navigation_type"))){
				minserMap.put("advertise_version", KvHelper.upCode("stVersion"));
			}
			
			DbUp.upTable("fh_app_navigation").dataInsert(minserMap);
	
		return mResult;
	}
}
