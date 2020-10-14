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

public class JobForRemindAfterSale extends RootJob{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	
	
	@Override
	public void doExecute(JobExecutionContext context) {
		String  baiduPushUrl = bConfig("familyhas.baidu_push_url");
		List<Map<String,Object>> pushList = new ArrayList<Map<String,Object>>();
		String sql = "SELECT * FROM newscenter.nc_aftersale_push_news WHERE response_status != 1 AND push_times <= 3";//response_status 不等于 1是取推送未成功的，且推送次数少于三次的。
		pushList = DbUp.upTable("nc_aftersale_push_news").dataSqlList(sql,null);
		for(Map<String,Object> map : pushList) {
			if(map != null) {
				String result = this.push(map,baiduPushUrl);
				String api_timespan = DateUtil.getNowTime();
				JSONObject jo = new JSONObject(result);
				Integer status = 0;
				String resultMessage = "";
				if(jo!=null) {
					status = jo.getInt("resultCode");
					resultMessage = jo.getString("resultMessage");
				}
				map.put("response_time", api_timespan);
				map.put("response_status", status);
				Integer pushTimes = Integer.parseInt(map.get("push_times").toString());
				pushTimes += 1;
				map.put("push_times", pushTimes.toString());
				map.put("push_url", baiduPushUrl);
				MDataMap mDataMap = new MDataMap(map);
				DbUp.upTable("nc_aftersale_push_news").dataUpdate(mDataMap, "response_time,response_status,push_times,push_url", "uid");
			}
		}
	}
	
	/**
	 * 推送方法
	 * @param map
	 */
	private String push(Map<String, Object> data,String url) {
		String memberCode = data.get("member_code").toString();
		if(StringUtils.isEmpty(memberCode)) {
			return "{\"resultCode\":0,\"resultMessage\":\"用户编号为空\"}";
		}
		MDataMap phoneMap = DbUp.upTable("mc_login_info").one("member_code",memberCode);
		String phone = "";
		if(phoneMap != null && !phoneMap.isEmpty()) {
			phone = phoneMap.get("login_name");
		}
		if(StringUtils.isEmpty(phone)) {
			return "{\"resultCode\":0,\"resultMessage\":\"根据用户编号获取的手机号为空\"}";
		}
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("toPage", "13");
		mDataMap.put("toUrl", data.get("after_sale_code").toString());
		String str = data.get("message").toString();
		try {
			mDataMap.put("msgContent", URLEncoder.encode(str, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		mDataMap.put("phone",phone);
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
