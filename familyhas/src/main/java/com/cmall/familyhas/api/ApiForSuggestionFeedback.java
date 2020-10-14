package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForSuggestionFeedbackInput;
import com.cmall.productcenter.util.Base64Util;
import com.srnpr.xmassystem.util.XSSUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 意见反馈接口
 * @author liqt
 *
 */
public class ApiForSuggestionFeedback extends
		RootApiForMember<RootResultWeb, ApiForSuggestionFeedbackInput>{
	
	public RootResultWeb Process(ApiForSuggestionFeedbackInput input,
			MDataMap mResquestMap){
		RootResultWeb result=new RootResultWeb();
		try {
			// 获取系统时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sysTime = sdf.format(new Date()).toString();
			
			String suggestionFeedback = Base64Util.getFromBASE64(input.getSuggestionFeedback());//base64解密 
			
			// 存在非法脚本代码
			if(XSSUtils.hasXSS(suggestionFeedback)) {
				return result;
			}
			
			String serialNumber=input.getSerialNumber();
			if(new String(suggestionFeedback.getBytes(), "UTF-8").length()>499){
				result.setResultCode(916401235);
				result.setResultMessage(bInfo(916401235));
				return result;
			}
			MDataMap mDataMap=DbUp.upTable("lc_client_info")
					.oneWhere("model,os,version,net_type,create_user", "", "", "order_code",serialNumber);
			
			if(mDataMap==null || StringUtils.isBlank(serialNumber)){
				MDataMap insertDatamap=new MDataMap();
				if(getFlagLogin()){
					insertDatamap.put("commit_user", getOauthInfo().getLoginName());
				}
				insertDatamap.put("suggestion_feedback", suggestionFeedback);
				insertDatamap.put("update_time", sysTime);
				DbUp.upTable("lc_suggestion_feedback").dataInsert(insertDatamap);
			}else{
				MDataMap insertDatamap=new MDataMap();
				if(getFlagLogin()){
					insertDatamap.put("commit_user", getOauthInfo().getLoginName());
				}
				insertDatamap.put("version",mDataMap.get("version"));
				insertDatamap.put("os", mDataMap.get("os"));
				insertDatamap.put("model", mDataMap.get("model"));
				insertDatamap.put("net_type", mDataMap.get("net_type"));
				insertDatamap.put("suggestion_feedback", suggestionFeedback);
				insertDatamap.put("update_time", sysTime);
				insertDatamap.put("app_code", getManageCode());
				
				DbUp.upTable("lc_suggestion_feedback").dataInsert(insertDatamap);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
