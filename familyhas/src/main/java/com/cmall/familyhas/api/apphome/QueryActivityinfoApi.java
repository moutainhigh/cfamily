package com.cmall.familyhas.api.apphome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cmall.familyhas.api.input.apphome.AppHomeActivityInput;
import com.cmall.familyhas.api.result.apphome.AppHomeActivityResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class QueryActivityinfoApi extends RootApiForToken<AppHomeActivityResult,AppHomeActivityInput> {


	@Override
	public AppHomeActivityResult Process(AppHomeActivityInput inputParam,
			MDataMap mRequestMap) {
		String uid = inputParam.getUid();
		String member_code = getUserCode();
		MDataMap mDataMap = DbUp.upTable("fh_apphome_channel_details").one("uid",uid);
		AppHomeActivityResult result = new AppHomeActivityResult();
		MDataMap checkUserJoined = DbUp.upTable("fh_apphome_activity_user").one("activity_uid",uid,"member_code",member_code);
		if(checkUserJoined != null){
			result.setIs_joined(true);
		}
		result.zid = Integer.parseInt(mDataMap.get("zid"));
		result.uid = mDataMap.get("uid");
		result.title = mDataMap.get("title");
		result.start_time = mDataMap.get("start_time");
		result.end_time = mDataMap.get("end_time");
		result.pic_url = mDataMap.get("pic_url");
		result.activity_location = mDataMap.get("activity_location");
		result.join_up_end_time = mDataMap.get("join_up_end_time");
		result.activity_max_people = Integer.parseInt(mDataMap.get("activity_max_people"));
		result.channel_uid = mDataMap.get("channel_uid");
		result.seq = Integer.parseInt(mDataMap.get("seq"));
		result.need_info = mDataMap.get("need_info");
		result.activity_desc = mDataMap.get("activity_desc");
		result.share_desc = mDataMap.get("share_desc");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		String activity_start_time = mDataMap.get("activity_start_time");
		String join_up_end_time = mDataMap.get("join_up_end_time");
		String weekStr = "";
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		try {
			Date activityStrat = sdf.parse(activity_start_time);
			Date joinUpEnd = sdf.parse(join_up_end_time);
			long l1 = joinUpEnd.getTime() - nowDate.getTime();
			long l2 = activityStrat.getTime() - nowDate.getTime();
			if(l1 < 0){//报名时间已结束
				result.is_time_out = true;
			}
			if(l2<0){//活动举办时间已结束不允许报名
				result.is_time_out = true;
			}
			if(l1>0){
				result.is_time_out_remark = "立即报名";
			}
			if(l1<0 && l2>0){
				result.is_time_out_remark = "报名已截止";
			}
			if(l2 < 0){
				result.is_time_out_remark = "已结束";
			}
			//
			cal.setTime(activityStrat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateStr = activity_start_time.substring(0, 16);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        weekStr = weekDays[w];
		String activity_start_time_str = dateStr + " "+weekStr;
		result.activity_start_time = activity_start_time_str;
		return result;
	}


}
