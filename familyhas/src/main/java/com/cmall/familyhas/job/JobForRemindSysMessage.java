package com.cmall.familyhas.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;

import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForRemindSysMessage extends RootJob{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	
	
	@Override
	public void doExecute(JobExecutionContext context) {
		String  baiduPushUrl = bConfig("familyhas.baidu_push_url");
		List<Map<String,Object>> pushList = new ArrayList<Map<String,Object>>();
		String sql = "SELECT * FROM familyhas.fh_message_notification WHERE status = '4497469400030002' AND if_push = '4497477800090001' AND push_success_time = '' AND push_time < sysdate()";
		pushList = DbUp.upTable("fh_message_notification").dataSqlList(sql,null);
		for(Map<String,Object> map : pushList) {
			if(map != null) {
				String result = this.push(map,baiduPushUrl);
				String api_timespan = DateUtil.getNowTime();
				JSONObject jo = new JSONObject(result);
				Integer status = 0;
				if(jo!=null) {
					status = jo.getInt("resultCode");
				}
				if(status == 1) {
					map.put("push_success_time", api_timespan);
					MDataMap mDataMap = new MDataMap(map);
					DbUp.upTable("fh_message_notification").dataUpdate(mDataMap, "push_success_time", "uid");
				}
			}
		}
	}
	
	/**
	 * 推送方法
	 * @param map
	 */
	private String push(Map<String, Object> data,String url) {
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("toPage", "15");
		mDataMap.put("toUrl", "");
		String str = data.get("content").toString();
		try {
			mDataMap.put("msgContent", URLEncoder.encode(str, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		mDataMap.put("phone","");//为空时全局推送
		ObjectMapper om = new ObjectMapper();
		String api_input = "";
		try {
			api_input = om.writeValueAsString(mDataMap);
			map.put("api_input", api_input);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String apiSecret =  api_target +api_key+ api_input + api_timespan +  MD5key;
		apiSecret = SecrurityHelper.MD5(apiSecret);
		map.put("api_secret", apiSecret.toLowerCase());
		String result = Http_Request_Post.doPost(url, map, "utf-8");
		MDataMap pushLog = new  MDataMap();
		pushLog.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
		pushLog.put("request_date", map.toString());
		pushLog.put("response_data", result);
		pushLog.put("url", url);
		pushLog.put("push_target", api_target);
		pushLog.put("api_input", api_input);
		pushLog.put("create_time", api_timespan);
		pushLog.put("response_time", DateUtil.getNowTime());
		DbUp.upTable("lc_push_news_log").dataInsert(pushLog);
		return result;
	}

}
