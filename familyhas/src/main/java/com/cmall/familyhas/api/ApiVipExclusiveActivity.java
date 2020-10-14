package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiVipExclusiveActivityInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveActivityResult;
import com.cmall.familyhas.model.VipActivity;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusiveActivity extends RootApiForToken<ApiVipExclusiveActivityResult, ApiVipExclusiveActivityInput> {
	
	private static final String ACTITITY_TYPE = "449748130004";
	
	@SuppressWarnings("deprecation")
	@Override
	public ApiVipExclusiveActivityResult Process(ApiVipExclusiveActivityInput inputParam, MDataMap mRequestMap) {
		int page = inputParam.getPage() - 1;//当前页数
		int pageCount = inputParam.getPageCount();//每页记录个数
		int start = page * pageCount;
		
		String dataHeadSql = "select d.uid, d.title, d.pic_url, d.activity_location, date_format(d.activity_start_time, '%Y/%m/%d') activity_time, (dayofweek(d.activity_start_time) - 1) week_num,"
				+ " (case when d.activity_start_time > sysdate() then '0' else '1' end) is_finish";
		String countHeadSql = "select count(1)";
		String commonSql = " from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '" + ACTITITY_TYPE + "' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate()";
		commonSql += " order by d.seq";
		
		String dataSql = dataHeadSql + commonSql + " limit " + start + "," + pageCount;
		List<Map<String, Object>> list = DbUp.upTable("fh_apphome_channel").dataSqlList(dataSql, new MDataMap());
		
		String countSql = countHeadSql + commonSql;
		int totalCount = DbUp.upTable("fh_apphome_channel").upTemplate().queryForInt(countSql, new HashMap<String, Object>());
		
		ApiVipExclusiveActivityResult apiResult = new ApiVipExclusiveActivityResult();
		List<VipActivity> activityList = new ArrayList<VipActivity>();
		for(Map<String, Object> map : list) {
			String weekStr = "";
			String weekNum = map.get("week_num") == null ? "" : map.get("week_num").toString();
			if("0".equals(weekNum)) {
				weekStr = "周日";
			}else if("1".equals(weekNum)) {
				weekStr = "周一";
			}else if("2".equals(weekNum)) {
				weekStr = "周二";
			}else if("3".equals(weekNum)) {
				weekStr = "周三";
			}else if("4".equals(weekNum)) {
				weekStr = "周四";
			}else if("5".equals(weekNum)) {
				weekStr = "周五";
			}else if("6".equals(weekNum)) {
				weekStr = "周六";
			}
			
			VipActivity activity = new VipActivity();
			activity.setActivityCode(map.get("uid") == null ? "" : map.get("uid").toString());
			activity.setTitle(map.get("title") == null ? "" : map.get("title").toString());
			activity.setActivityTime(map.get("activity_time") == null ? "" : map.get("activity_time").toString());
			activity.setPicUrl(map.get("pic_url") == null ? "" : map.get("pic_url").toString());
			activity.setActivityLocation(map.get("activity_location") == null ? "" : map.get("activity_location").toString());
			activity.setWeekNum(weekStr);
			activity.setIsFinish(map.get("is_finish") == null ? "" : map.get("is_finish").toString());
			activityList.add(activity);
		}
		apiResult.setItemList(activityList);
		apiResult.setTotalPage(totalCount);
		
		//添加微信分享信息
		String shareSql = "select c.channel_name, c.share_title, c.is_share, c.share_desc, c.share_pic from fh_apphome_channel c where c.channel_type = '" + ACTITITY_TYPE + "' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> shareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(shareSql, new MDataMap());
		if(shareMap != null) {
			apiResult.setChannelName(shareMap.get("channel_name") == null ? "" : shareMap.get("channel_name").toString());
			apiResult.setShareTitle(shareMap.get("share_title") == null ? "" : shareMap.get("share_title").toString());
			apiResult.setIsShare(shareMap.get("is_share") == null ? "" : shareMap.get("is_share").toString());
			apiResult.setShareDesc(shareMap.get("share_desc") == null ? "" : shareMap.get("share_desc").toString());
			apiResult.setSharePic(shareMap.get("share_pic") == null ? "" : shareMap.get("share_pic").toString());
		}
		return apiResult;
	}

}
