package com.cmall.familyhas.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForRemindSign extends RootJob{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	
	@Override
	public void doExecute(JobExecutionContext context) {
		List<String> userAllNums = getUserAllNums();
		StringBuilder sb =  new StringBuilder();
		int countPs = 0;
		for(int i = 0;i<userAllNums.size();i++) {
			String num = userAllNums.get(i);
			if(i!=0 && i%999==0) {
				sb.append(num);
				//处理这100条数据
				String processNums = processNums(sb.toString());
				if(processNums.contains("\"resultCode\":1")) {
					insertNumLog(sb.toString());
					countPs++;
					if(countPs==9) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
							countPs = 0;
						}
					}
				};
				//重置nums
				sb =  new StringBuilder();
			}else {
				if(i<userAllNums.size()-1) {
					sb.append(num+",");
				}else {
					sb.append(num);
				}
			}
		}
		if(!"".equals(sb.toString())) {
			//处理不足1000条的数据
			String processNums = processNums(sb.toString());
			if(processNums.contains("\"resultCode\":1")) {
				insertNumLog(sb.toString());
			};
		}
	}
	
	
	private String processNums(String nums) {
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		MDataMap mDataMap = new MDataMap();
		String str = "亲,您今天还没签到,记得签到哦";
		try {
			mDataMap.put("msgContent", URLEncoder.encode(str, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		mDataMap.put("phone",nums);
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
		String  baiduPushUrl = bConfig("familyhas.baidu_push_url");
		String result = Http_Request_Post.doPost(baiduPushUrl, map, "utf-8");
		return result;
	}

	private List<String> getUserAllNums() {
		String sql = "select a.user_code,b.login_name from mc_sign a join mc_login_info b on a.user_code = b.member_code where a.flag_sign_today = 0 and a.sign_remind_flag = 1";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_sign").dataSqlList(sql, null);
		List<String> allNums = new ArrayList<String>();
		for(Map<String, Object> map : dataSqlList) {
			String num = map.get("login_name").toString();
			allNums.add(num);
		}
		return allNums;
	}
	private void insertNumLog(String processNums) {
		MDataMap updateMap = new MDataMap();
		updateMap.put("rec_remind_tel_num", processNums);
		String nowTime = DateUtil.getNowTime();
		updateMap.put("rec_time", nowTime);
		DbUp.upTable("lc_sign_remind_log").dataInsert(updateMap);
	}

	
}
