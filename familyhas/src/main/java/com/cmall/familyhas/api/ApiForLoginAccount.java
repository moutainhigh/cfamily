package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForLoginAccountInput;
import com.cmall.familyhas.api.result.ApiForLoginAccountResult;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webmodel.MOauthScope;
import com.srnpr.zapweb.websupport.ApiCallSupport;
/***
 * 微公社惠家有账户互通
 * @author xiegj
 *
 */
public class ApiForLoginAccount extends RootApiForManage<ApiForLoginAccountResult,ApiForLoginAccountInput> {

	public ApiForLoginAccountResult Process(ApiForLoginAccountInput inputParam, MDataMap mRequestMap) {
		ApiForLoginAccountResult result=new ApiForLoginAccountResult();
		
		//mc_member_info   三个字段确定唯一，没有则插进去一条数据
		//mc_login_info 三个字段确定唯一，没有则插进去一条数据
		//mc_account_info  根据account_code判断，没有则插进去一条数据
		//za_oauth 直接插入一条数据。manage_code为SI2003
		
		String loginName = inputParam.getLoginName();
		String loginPass = inputParam.getLoginPass();
		String loginToken = inputParam.getLoginToken();
		String memberCode = inputParam.getMemberCode();
		String accountCode = inputParam.getAccountCode();
		String manageCode = AppConst.MANAGE_CODE_HOMEHAS;		//SI2003
		String unionId = inputParam.getUnionId();
		String openid_gzh = inputParam.getOpenid_gzh();
		String openid_app = inputParam.getOpenid_app();
		String openid_xch = inputParam.getOpenid_xch();
		
		int mamNum = DbUp.upTable("mc_member_info").count("member_code",memberCode,"account_code",accountCode,"manage_code",manageCode);
		
		int logNum = DbUp.upTable("mc_login_info").count("member_code",memberCode,"login_name",loginName,"manage_code",manageCode);
		
		int accNum = DbUp.upTable("mc_account_info").count("account_code",accountCode);
		//此接口为微公社提供调用，可能会出现传入数据不完整或错误到此会insert报错，
		//在此加上try，把错误记录以及错误信息都打印到数据库里
		try{
			if (mamNum == 0) {
				MDataMap mamMap = new MDataMap();
				mamMap.put("uid", WebHelper.upUuid());
				mamMap.put("member_code", memberCode);
				mamMap.put("account_code", accountCode);
				mamMap.put("manage_code", manageCode);
				mamMap.put("flag_enable", "1");
				mamMap.put("create_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("mc_member_info").dataInsert(mamMap);
			}
			if (logNum == 0) {
				MDataMap logMap = new MDataMap();
				logMap.put("uid", WebHelper.upUuid());
				logMap.put("login_code", manageCode+WebConst.CONST_SPLIT_DOWN+loginName);
				logMap.put("manage_code", manageCode);
				logMap.put("login_name", loginName);
				logMap.put("login_pass", loginPass);
				logMap.put("member_code", memberCode);
				logMap.put("create_time", FormatHelper.upDateTime());
				logMap.put("flag_enable", "1");
				logMap.put("login_type", "1010040001");
				logMap.put("login_group", MemberConst.LOGIN_GROUP_DEFAULT);
				logMap.put("unionId", unionId);
				logMap.put("openid_gzh", openid_gzh);
				logMap.put("openid_app", openid_app);
				logMap.put("openid_xch", openid_xch);
				DbUp.upTable("mc_login_info").dataInsert(logMap);
				//用户互通后,注册送券
				JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
						CouponConst.register_coupon,new MDataMap("mobile", loginName,"manage_code", manageCode));
			}else {//登录时只更新微信相关字段 20180408 -rhb
				MDataMap logMap = new MDataMap();
				logMap.put("unionId", unionId);
				logMap.put("openid_gzh", openid_gzh);
				logMap.put("openid_app", openid_app);
				logMap.put("openid_xch", openid_xch);
				logMap.put("member_code", memberCode);
				logMap.put("login_name", loginName);
				logMap.put("manage_code", manageCode);
				DbUp.upTable("mc_login_info").dataUpdate(logMap, "unionId,openid_gzh,openid_app,openid_xch", "member_code,login_name,manage_code");

			}
			
			if (accNum == 0) {
				MDataMap accMap = new MDataMap();
				accMap.put("uid", WebHelper.upUuid());
				accMap.put("account_code", accountCode);
				DbUp.upTable("mc_account_info").dataInsert(accMap);
			}
		}catch(Exception e){
			MDataMap logMap = new MDataMap();
			logMap.put("uid", WebHelper.upUuid());
			logMap.put("request_data",  new JsonHelper<ApiForLoginAccountInput>().ObjToString(inputParam));
			logMap.put("error_text", e.getMessage());
			logMap.put("request_time", FormatHelper.upDateTime());
			logMap.put("execute_target", this.getClass().getName());
			logMap.put("remark", "mc_member_info,mc_login_info,mc_account_info表插入数据有问题");
			DbUp.upTable("lc_execute_error_log").dataInsert(logMap);
			e.printStackTrace();
		}
		try{
			MDataMap oauMap = new MDataMap();
			oauMap.put("uid", WebHelper.upUuid());
			oauMap.put("auth_code", WebHelper.upUuid());
			oauMap.put("access_token", loginToken);
			oauMap.put("user_code", memberCode);
			oauMap.put("manage_code", manageCode);
			oauMap.put("create_time", FormatHelper.upDateTime());
			oauMap.put("expires_time", DateHelper.upDateTimeAdd(MemberConst.OAUTH_EXPIRESS_TIME));
			oauMap.put("scope_resources","");
			oauMap.put("flag_enable", "1");
			oauMap.put("login_name", loginName);
			// 设置授权类型
			MOauthScope mOauthScope = new MOauthScope();
			mOauthScope.setManageCode(manageCode);
			mOauthScope.setScopeType("oauth");
			oauMap.put("scope_resources",  GsonHelper.toJson(mOauthScope));
			
			if(DbUp.upTable("za_oauth").count("access_token",loginToken) == 0) {
				DbUp.upTable("za_oauth").dataInsert(oauMap);
			}

			// 如果不是用户中心返回的token，则同步一份到用户中心
//			if(!"hjy_mem".equalsIgnoreCase(inputParam.getProjectCode())){
//				SyncTokenInput in = new SyncTokenInput();
//				in.setAccessToken(loginToken);
//				in.setLoginName(loginName);
//				in.setMemberCode(memberCode);
//				
//				ApiCallSupport<RootInput, RootResult> apiCallSupport = new ApiCallSupport<RootInput, RootResult>();
//				try {
//					RootResult res = apiCallSupport.doCallUserCenterApiForToken(
//							bConfig("xmassystem.hjyBeanUrl"), 
//							"1015", 
//							bConfig("xmassystem.hjyBeanKey"), 
//							bConfig("xmassystem.hjyBeanMD5Key"), 
//							"", 
//							in,
//							new RootResult());
//					if(res.getResultCode() != 1){
//						LogFactory.getLog(getClass()).warn("同步TOKEN到用户中心失败: "+res.getResultMessage()+" ["+memberCode+","+loginToken+"]");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			//解绑此微信号绑定的其他账号
			if(StringUtils.isBlank(unionId)) {
				int count = DbUp.upTable("mc_member_wechat_bound").dataCount("member_code=:member_code and wechat_code!='' ", new MDataMap("member_code",memberCode));
				if(count>0) {
					//存在 记录用户还未解绑，做解绑操作
					DbUp.upTable("mc_member_wechat_bound").dataExec("update mc_member_wechat_bound set is_issue='0',is_bound='0',wechat_code='',nick_name_wechat=''  where  member_code=:member_code ", new MDataMap("member_code",memberCode));
				}
			}else {
				int count = DbUp.upTable("mc_member_wechat_bound").dataCount("member_code=:member_code and wechat_code!=:wechat_code ", new MDataMap("member_code",memberCode,"wechat_code",unionId));
                if(count>0) {
                	//存在记录中的用户绑定非此微信账号，做重新绑定并解绑其他用户操作
                	DbUp.upTable("mc_member_wechat_bound").dataExec("update mc_member_wechat_bound set is_issue='0',is_bound='0',wechat_code='',nick_name_wechat=''  where  wechat_code=:wechat_code and  member_code!=:member_code ", new MDataMap("member_code",memberCode,"wechat_code",unionId));
    				DbUp.upTable("mc_member_wechat_bound").dataExec("update mc_member_wechat_bound set is_bound='1',wechat_code='"+unionId+"',is_issue='0'  where member_code=:member_code ", new MDataMap("member_code",memberCode));
                }	
			}
		
	
			
		}catch(Exception e){
			MDataMap logMap = new MDataMap();
			logMap.put("uid", WebHelper.upUuid());
			logMap.put("request_data",  new JsonHelper<ApiForLoginAccountInput>().ObjToString(inputParam));
			logMap.put("error_text", e.getMessage());
			logMap.put("request_time", FormatHelper.upDateTime());
			logMap.put("execute_target", this.getClass().getName());
			logMap.put("remark", "za_oauth表插入数据有问题");
			DbUp.upTable("lc_execute_error_log").dataInsert(logMap);
			e.printStackTrace();
		}
		return result;
	}
	
	static class SyncTokenInput extends RootInput{
		private String memberCode;
		private String accessToken;
		private String loginName;
		public String getMemberCode() {
			return memberCode;
		}
		public void setMemberCode(String memberCode) {
			this.memberCode = memberCode;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public String getLoginName() {
			return loginName;
		}
		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}
	}
	
	
}

