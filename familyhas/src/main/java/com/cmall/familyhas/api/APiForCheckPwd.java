package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiForCheckPwdInput;
import com.cmall.familyhas.api.result.APiForCheckPwdResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.cmall.membercenter.group.model.GroupLoginInput;
import com.cmall.membercenter.group.model.GroupLoginResult;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.systemcenter.util.AESUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.websupport.ApiCallSupport;


/** 
* @ClassName: APiForCheckPwd 
* @Description: 惠家有校验密码
* @author 张海生
* @date 2015-8-18 下午12:44:12 
*  
*/
public class APiForCheckPwd extends
		RootApiForToken<APiForCheckPwdResult, APiForCheckPwdInput> {
	public APiForCheckPwdResult Process(APiForCheckPwdInput inputParam,
			MDataMap mRequestMap) {
		AESUtil an = new AESUtil();
		an.initialize();
		String pwd = an.decrypt(inputParam.getPassword());//decrypt解密
		APiForCheckPwdResult result = new APiForCheckPwdResult();
		String loginName = getOauthInfo().getLoginName();
//		MemberLoginSupport ls = new MemberLoginSupport();
		GroupLoginInput in = new GroupLoginInput();
		in.setLoginName(loginName);
		in.setLoginPass(pwd);
//		GroupLoginResult re = ls.doGroupLogin(in, getManageCode());
		GroupLoginResult re = groupLoginForApi(in, getManageCode());
		String memberCode = getUserCode();
		String failCount = XmasKv.upFactory(EKvSchema.PasswordCheck).hget(memberCode, "count");
		String failTime = XmasKv.upFactory(EKvSchema.PasswordCheck).hget(memberCode, "time");
		int minute = 0;
		if(StringUtils.isNotEmpty(failTime)){
			int second = DateUtil.timeSubtraction(failTime, DateUtil.getNowTime(), DateUtil.DATE_FORMAT_DATETIME);
			minute = second/60;
		}
		if(StringUtils.isEmpty(failCount)){
			XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "count", "0");
			failCount = XmasKv.upFactory(EKvSchema.PasswordCheck).hget(memberCode, "count");
		}
		if(!re.upFlagTrue()){//密码输错的时候
			if("0".equals(failCount) || minute/60 >= 3){
				XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "count", "1");
				XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "time", DateUtil.getNowTime());
			}else if(Integer.parseInt(failCount) < 3){//失败次数加1,失败时间更新为最新时间
				XmasKv.upFactory(EKvSchema.PasswordCheck).hincrBy(memberCode, "count", 1);
				XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "time", DateUtil.getNowTime());
			}
			int finalCount = Integer.parseInt(XmasKv.upFactory(EKvSchema.PasswordCheck).hget(memberCode, "count"));
			if(finalCount == 1){
				result.setResultCode(2);
				result.setResultMessage("密码不正确，还可以输入2次。");
			}else if(finalCount == 2){
				result.setResultCode(3);
				result.setResultMessage("密码不正确，还可以输入1次。");
			}else  if(finalCount == 3){
				int remain = 180 - minute;
				String st = getTime(remain);
				result.setRemainMinute(remain);
				result.setResultCode(4);
				result.setResultMessage("密码连续输入错误3次，余额被锁定，请"+st+"后重试!");
			}else{
				result.setResultCode(0);
				result.setResultMessage("密码错误!");
			}
		}else{//密码输正确的时候
			if(minute/60 >= 3){
				XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "count", "0");
				XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "time", "");
			}else{
				int finalCount = Integer.parseInt(XmasKv.upFactory(EKvSchema.PasswordCheck).hget(memberCode, "count"));
				if(finalCount == 3){//如果已经输错3次了
					int remain = 180 - minute;
					String st = getTime(remain);
					result.setRemainMinute(remain);
					result.setResultCode(4);
					result.setResultMessage("密码连续输入错误3次，余额被锁定，请"+st+"分钟后重试!");
				}else{//把错误记录从缓存中清空
					XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "count", "0");
					XmasKv.upFactory(EKvSchema.PasswordCheck).hset(memberCode, "time", "");
				}
			}
		}
		if(result.upFlagTrue()){
			GroupAccountService accountService = new GroupAccountService();
//			GroupAccountInfoResult gr= accountService.getAccountInfo(accountService.getAccountCodeByMemberCode(memberCode));
			GroupAccountInfoResult gr= accountService.getAccountInfoByApi(memberCode);
			if("0".equals(gr.getFlagEnable())){
				result.setResultCode(916421164);
				result.setResultMessage(bInfo(916421164));
			}else if (Double.valueOf(gr.getWithdrawMoney())==0) {
				result.setResultCode(916421166);
				result.setResultMessage(bInfo(916421166));
			}
		}
		return result;
	}
	
	/** 
	* @Description:获取时间
	* @param minute 分钟
	* @author 张海生
	* @date 2015-8-26 下午6:31:18
	* @return String 
	* @throws 
	*/
	public String getTime(int minute){
		String result = "";
		int hour = minute/60;
		int mt = minute % 60;
		if(hour > 0 && mt > 0){
			result = hour + "小时" + mt + "分钟";
		}else if(hour > 0 && mt == 0){
			result = hour + "小时";
		}else{
			result =  mt + "分钟";
		}
		return result;
	}
	
	private GroupLoginResult groupLoginForApi(GroupLoginInput in,String sManageCode){
		
		ApiCallSupport<GroupLoginInput, GroupLoginResult> apiCallSupport = new ApiCallSupport<GroupLoginInput, GroupLoginResult>();
		
		GroupLoginResult groupLoginResult = new GroupLoginResult();
		
		try {
			groupLoginResult = apiCallSupport.doCallApi(
					bConfig("xmassystem.group_pay_url"),
					bConfig("xmassystem.group_pay_login_face"),
					bConfig("xmassystem.group_pay_key"),
					bConfig("xmassystem.group_pay_pass"), in,
					groupLoginResult);
		} catch (Exception e) {
			groupLoginResult.setResultCode(-100);
			e.printStackTrace();
		}
		
		return groupLoginResult;
	}
}
