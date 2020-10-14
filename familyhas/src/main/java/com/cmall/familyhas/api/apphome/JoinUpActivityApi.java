package com.cmall.familyhas.api.apphome;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.apphome.ActivityJoinUpInput;
import com.cmall.familyhas.api.result.apphome.ActivityJoinUpResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class JoinUpActivityApi extends RootApiForToken<ActivityJoinUpResult,ActivityJoinUpInput> {
	
	private static final String NAME_CODE = "449748140001";//姓名
	private static final String PHONE_CODE = "449748140002";//电话
	private static final String CARD_CODE = "449748140003";//身份证号
	private static final String CITY_CODE = "449748140004";//城市
	
	private static final String STR_REG = "^[\u4e00-\u9fbb]+$";//校验中文正则表达式
	private static final String INT_REG = "[0-9]+";//校验数字正则表达式

	@Override
	public ActivityJoinUpResult Process(ActivityJoinUpInput inputParam,
			MDataMap mRequestMap) {
		
		String member_code = getUserCode();
		String name = inputParam.getName();
		String phone = inputParam.getPhone();
		String personal_num = inputParam.getPersonal_num();
		String city = inputParam.getCity();
		String activity_uid = inputParam.getActivity_uid();
		MDataMap channel_details = DbUp.upTable("fh_apphome_channel_details").one("uid",activity_uid);
		String need_info = channel_details.get("need_info");
		ActivityJoinUpResult result = new ActivityJoinUpResult();
		if(need_info.contains(CARD_CODE)){//身份证为必填项
			if(StringUtils.isBlank(personal_num)){
				result.setResultCode(10000);
				result.setResultMessage("身份证号不能为空");
				return result;
			}
			Boolean flag = false;
			if(personal_num.length()== 18){
				String checkNum = (String) personal_num.subSequence(0, 17);
				flag = checkNum.matches(INT_REG);
			}
			if(personal_num.length() == 15){
				flag = personal_num.matches(INT_REG);
			}
			if(!flag){
				result.setResultCode(10000);
				result.setResultMessage("身份证号不合法");
				return result;
			}
		}
		MDataMap insertMap = new MDataMap();
		if(need_info.contains(CITY_CODE)){
			if(StringUtils.isBlank(city)){
				result.setResultCode(10000);
				result.setResultMessage("所在城市不能为空");
				return result;
			}
		}
		if(need_info.contains(PHONE_CODE)){
			if(StringUtils.isBlank(phone)){
				result.setResultCode(10000);
				result.setResultMessage("电话不能为空");
				return result;
			}
			
			if(phone.length() != 11){
				result.setResultCode(10000);
				result.setResultMessage("手机号码不合法");
				return result;
			}
			if(!phone.matches(INT_REG)){
				result.setResultCode(10000);
				result.setResultMessage("手机号码不合法");
				return result;
			}
		}
		if(need_info.contains(NAME_CODE)){
			if(StringUtils.isBlank(name)){
				result.setResultCode(10000);
				result.setResultMessage("姓名不能为空");
				return result;
			}
			if(name.length()>5){
				result.setResultCode(10000);
				result.setResultMessage("姓名不合法");
				return result;
			}
			if(!name.matches(STR_REG)){
				result.setResultCode(10000);
				result.setResultMessage("姓名不合法");
				return result;
			}
		}
		insertMap.put("member_code", member_code);
		insertMap.put("activity_uid", activity_uid);
		if(!StringUtils.isBlank(name)){
			insertMap.put("name", name);
		}
		if(!StringUtils.isBlank(phone)){
			insertMap.put("phone", phone);		
		}
		if(!StringUtils.isBlank(personal_num)){
			insertMap.put("personal_num", personal_num);
		}
		if(!StringUtils.isBlank(city)){
			insertMap.put("city", city);
		}
		
		DbUp.upTable("fh_apphome_activity_user").dataInsert(insertMap);
		return result;
	}
	

}
