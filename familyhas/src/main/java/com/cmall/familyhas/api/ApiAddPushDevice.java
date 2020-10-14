package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiAddPushDeviceInput;
import com.cmall.familyhas.model.DevicePushModel;
import com.cmall.familyhas.service.DevicePushService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 添加推送的设备信息
 * 
 * @author fq
 * 
 */
public class ApiAddPushDevice extends
		RootApiForManage<RootResult, ApiAddPushDeviceInput> {

	@Override
	public RootResult Process(ApiAddPushDeviceInput inputParam,
			MDataMap mRequestMap) {
		RootResult result = new RootResult();

		String channelId = inputParam.getChannelId();
		String deviceToken = inputParam.getDeviceToken();
		String noticeSwitch = inputParam.getNoticeSwitch();
		
		//判断是否有token，没有token则进行解绑
		if(StringUtils.isNotBlank( inputParam.getToken()) && StringUtils.isNotBlank( inputParam.getUserPhone())) {
			
			if(!this.mobileReg(inputParam.getUserPhone())) {
				result.setResultCode(0);
				result.setResultMessage("手机号格式错误");
				return result;
			}
			List<MDataMap> queryByWhere = new ArrayList<>();
			//查询该设备和该用户是否已经是绑定状态
			if(!"".equals(deviceToken)) {
				queryByWhere = DevicePushService.queryByWhere("mobile",inputParam.getUserPhone(),"device_token",deviceToken,"bund_status","1001","notice_switch",noticeSwitch);
			}else  if(!"".equals(channelId)){
				queryByWhere = DevicePushService.queryByWhere("mobile",inputParam.getUserPhone(),"channel_id",inputParam.getChannelId(),"bund_status","1001");
			}
			if(queryByWhere.size() > 0) {
				return result;
			}
			
			//删除用户（userPhone）绑定过的设备号  及   绑定该设备（channelId）的 用户
			DevicePushService.delByWhere("mobile",inputParam.getUserPhone());
			DevicePushService.delByWhere("channel_id",inputParam.getChannelId());
//			if(!"".equals(deviceToken)) {
//				DevicePushService.delByWhere("device_token",deviceToken);
//			}
			
			DevicePushModel model = new DevicePushModel();
			model.setApp_vision(inputParam.getApp_vision());
			model.setBund_status("1001");
			model.setChannel_id(inputParam.getChannelId());
			model.setDevice_type(inputParam.getDeviceType());
			model.setMobile(inputParam.getUserPhone());
			
			if(!"".equals(deviceToken)) {
				model.setDevice_token(deviceToken);
				model.setNoticeSwitch(inputParam.getNoticeSwitch());
			}
			
			try {
				DevicePushService.addDeviceInfo(model);
			}catch(Exception e) {
				
			}
			
		} else {
			/*
			 * 解绑
			 */
			DevicePushService.delByWhere("channel_id",inputParam.getChannelId(),"bund_status","1001");
			
		}

		return result;
	}
	
	private boolean mobileReg(String mobile) {
		String regEx = "^1[0-9]{10}$";
		Pattern p = Pattern.compile(regEx);
		if(p.matcher(mobile).find()) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
