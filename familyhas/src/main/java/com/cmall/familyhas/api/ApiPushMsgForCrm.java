package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.json.JSONObject;

import com.cmall.familyhas.api.input.PushMsgInput;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnpr.xmassystem.duohuozhu.utils.MD5Util;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiPushMsgForCrm extends RootApi<RootResult,PushMsgInput>{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	@Override
	public RootResult Process(PushMsgInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult msgSendResult = new RootResult();
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		
		MDataMap mDataMap = new MDataMap();
		if(inputParam.getPush_type().equals("01")){
			mDataMap.put("toPage", "8");			
		}else if(inputParam.getPush_type().equals("02")){
			mDataMap.put("toPage", "8");
			String ea = inputParam.getPush_url();
			ea=(ea==null?"":ea);
			String signed = MD5Util.MD5Encode(ea+"jiayougo", "utf-8");
			String url = TopUp.upConfig("xmassystem.pnm_url")+ea+"&sign="+signed;
			inputParam.setPush_url(url);
		}else if(inputParam.getPush_type().equals("03")){
			mDataMap.put("toPage", "2");
		}		
		try {
			mDataMap.put("toUrl", URLEncoder.encode(inputParam.getPush_url()+"|"+inputParam.getOperate_id(),"utf-8"));
			mDataMap.put("msgContent", URLEncoder.encode(inputParam.getContent(), "utf-8"));
			mDataMap.put("title", URLEncoder.encode(inputParam.getTitle(), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		mDataMap.put("phone",inputParam.getMobile());
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
		String url = bConfig("familyhas.baidu_push_url");
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
		JSONObject jo = new JSONObject(result);
		msgSendResult.setResultCode(jo.getInt("resultCode"));
		msgSendResult.setResultMessage(jo.getString("resultMessage"));

		return msgSendResult;
	}
	
	

}
