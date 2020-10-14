package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 导航维护发布按钮
 * @author liqt
 *
 */
public class NavigationMaintainRelease extends RootFunc{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		MDataMap mDataMap2 = DbUp.upTable("fh_app_navigation").one("uid",uid);
		String releaseFlag = mDataMap2.get("release_flag");
		MDataMap mDataMap3 = new MDataMap();
		mDataMap3.put("uid", uid);
		if ("449746250001".equals(releaseFlag)) {// 如果现在为已发布
			mDataMap3.put("release_flag", "449746250002");
			DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap3,
					"release_flag", "uid");
		}
		if("449746250002".equals(releaseFlag)){//如果现在为未发布
			String channel_limit = mDataMap2.get("channel_limit");
			String channels = mDataMap2.get("channels");
			List<MDataMap> mList = new ArrayList<>();
			 mList = DbUp.upTable("fh_app_navigation").queryAll("channels,channel_limit,navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001"));
			String navigationType = mDataMap2.get("navigation_type");
			String startTime = mDataMap2.get("start_time");
			String endTime = mDataMap2.get("end_time");
			
			
			String nowTime = DateUtil.getNowTime();
			Boolean flag = true;
			//关于时间的一些规则
			for(MDataMap m : mList){
				if(!DateUtil.getTimefag(endTime, nowTime)){
					mResult.inErrorMessage(916421189);//该类型已过期，不能发布
				}else{
					if(!navigationType.equals(m.get("navigation_type")) || uid.equals(m.get("uid"))){
						continue;
					}else if(DateUtil.getTimefag(startTime, m.get("end_time")) //如果类型相同，时间不重叠
									|| DateUtil.getTimefag( m.get("start_time"),endTime)){
						flag=true;
					}else{
						//时间重叠，判断渠道是否相同 广告导航 悬浮导航
						if(("4497467900040007".equals(navigationType) || "4497467900040006".equals(navigationType))&&"4497471600070002".equals(channel_limit)&&"4497471600070002".equals(m.get("channel_limit"))) {
							//导航类型绑定的不同渠道，同样可以发布
							List<String> asList = new ArrayList<String>(Arrays.asList(channels.split(",")));
							List<String> asList2 = new ArrayList<String>(Arrays.asList(m.get("channels").split(",")));
							asList.retainAll(asList2);
							if(asList.size()>0) {
								flag=false;
								break;
							}else {
								continue;
							}
						}else {
							flag=false;
							break;
						}
					}
				}
			}
			if (flag) {
				mDataMap3.put("release_flag", "449746250001");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap3, "release_flag","uid");
			} else {
				mResult.inErrorMessage(916421190);// 相同类型中有时间重叠的部分，不能发布
			}
		}
		return mResult;
	}
	
}
