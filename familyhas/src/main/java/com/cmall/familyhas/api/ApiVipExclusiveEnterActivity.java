package com.cmall.familyhas.api;

import java.util.Map;

import com.cmall.familyhas.api.input.ApiVipExclusiveEnterInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveEnterResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusiveEnterActivity extends RootApiForToken<ApiVipExclusiveEnterResult, ApiVipExclusiveEnterInput> {
	
	private static final String NAME_CODE = "449748140001";//姓名
	private static final String PHONE_CODE = "449748140002";//电话
	private static final String CARD_CODE = "449748140003";//身份证号
	private static final String CITY_CODE = "449748140004";//城市
	
	@Override
	public ApiVipExclusiveEnterResult Process(ApiVipExclusiveEnterInput inputParam, MDataMap mRequestMap) {
		String activityCode = inputParam.getActivityCode();//活动编号，即uid
		String name = inputParam.getName();
		String phone = inputParam.getPhone();
		String city = inputParam.getCity();
		String personNum = inputParam.getPersonalNum();
		
		ApiVipExclusiveEnterResult apiResult = new ApiVipExclusiveEnterResult();
		Map<String, Object> activity = DbUp.upTable("fh_apphome_channel_details").dataSqlOne("select d.activity_max_people, d.need_info from fh_apphome_channel_details d where d.uid = '" + activityCode + "'", new MDataMap());
		if(activity != null) {
			boolean flag = true;
			String need_info = activity.get("need_info") == null ? "" : activity.get("need_info").toString();
			if(need_info.contains(NAME_CODE)) {
				if("".equals(name)) {
					flag = false;
				}
			}
			if(need_info.contains(PHONE_CODE)) {
				if("".equals(phone)) {
					flag = false;
				}
			}
			if(need_info.contains(CARD_CODE)) {
				if("".equals(personNum)) {
					flag = false;
				}			
			}
			if(need_info.contains(CITY_CODE)) {
				if("".equals(city)) {
					flag = false;
				}
			}
			
			if(flag) {
				//校验活动是否已满
				int maxPerson = activity.get("activity_max_people") == null ? 0 : Integer.parseInt(activity.get("activity_max_people").toString());
				int count = DbUp.upTable("fh_apphome_activity_user").count("activity_uid", activityCode);
				if(maxPerson > count) {
					count = DbUp.upTable("fh_apphome_activity_user").count("activity_uid", activityCode, "member_code", getUserCode());
					if(count > 0) {
						apiResult.setResultCode(0);
						apiResult.setResultMessage("该活动您已报名!");
					}else {
						MDataMap mdataMap = new MDataMap();
						mdataMap.put("activity_uid", activityCode);
						mdataMap.put("member_code", getUserCode());
						mdataMap.put("name", name);
						mdataMap.put("phone", phone);
						mdataMap.put("personal_num", personNum);
						mdataMap.put("city", city);
						DbUp.upTable("fh_apphome_activity_user").dataInsert(mdataMap);
						
						apiResult.setResultCode(1);
						apiResult.setResultMessage("报名成功!");
					}
				}else {
					apiResult.setResultCode(0);
					apiResult.setResultMessage("该活动已报满!");
				}
			}else {
				apiResult.setResultCode(0);
				apiResult.setResultMessage("必要参数未填写!");
			}
		}else {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("该活动不存在!");
		}
		return apiResult;
	}

}
