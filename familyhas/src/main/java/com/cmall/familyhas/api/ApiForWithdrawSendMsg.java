package com.cmall.familyhas.api;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForWithdrawSendMsgInput;
import com.cmall.familyhas.api.result.ApiForWithdrawSendMsgResult;
import com.cmall.systemcenter.enumer.EVerifyCodeTypeEnumer;
import com.cmall.systemcenter.message.SendMessageBase;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 用户惠币提现发短信验证码接口(专用)
 * @author lgx
 *
 */
public class ApiForWithdrawSendMsg extends RootApiForToken<ApiForWithdrawSendMsgResult, ApiForWithdrawSendMsgInput> {

	public ApiForWithdrawSendMsgResult Process(ApiForWithdrawSendMsgInput inputParam, MDataMap mRequestMap) {
		
		ApiForWithdrawSendMsgResult result = new ApiForWithdrawSendMsgResult();
		
		String userCode = getUserCode();
		
		String mobile = "";
		String sql = "SELECT login_name FROM mc_login_info WHERE member_code = '"+userCode+"' AND manage_code = 'SI2003' AND flag_enable = 1 ORDER BY create_time DESC LIMIT 1";
		Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne(sql, new MDataMap());
		if(login_info != null) {
			String login_name = MapUtils.getString(login_info, "login_name");
			if(isPhone(login_name)) {
				mobile = login_name;
				login_name = login_name.substring(0, 3) + "****" + login_name.substring(7);
				result.setMobilePhone(login_name);
			}else {
				result.setResultCode(-1);
				result.setResultMessage("提现手机号有误!");
				return result;
			}
		}
		
		SendMessageBase messageBase=new SendMessageBase();
		// 惠币提现获取验证码专用
		MWebResult sendVerifyCode = messageBase.sendVerifyCode(EVerifyCodeTypeEnumer.huiCoinsWithdraw, mobile, "SI2003", 6, 300);
		
		result.setResultCode(sendVerifyCode.getResultCode());
		result.setResultMessage(sendVerifyCode.getResultMessage());
		
		return result;
		
	}
	
	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}

}
