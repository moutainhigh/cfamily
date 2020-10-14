package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.MemberInfoInput;
import com.cmall.familyhas.api.result.MemberInfoResult;
import com.cmall.membercenter.txservice.TxMemberForStar;
import com.cmall.systemcenter.bill.HexUtil;
import com.cmall.systemcenter.bill.MD5Util;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * @title: com.cmall.familyhas.api.ApiMemberInfoCf.java 
 * @description: 获取、修改用户昵称，头像|存在惠家有数据库中，张威也会存在会员中心
 * @author ligj
 * @date 2015-12-28
 * @version 1.0.0
 * 
 * 
 * @description: 这个接口的逻辑将统一调用张威的接口，将昵称这些信息存入会员中心，以后也都从会员中心取数据
 * @author Yangcl
 * @date 2016年11月7日 上午10:18:16 
 * @version 1.0.1
 */
public class ApiMemberInfoCf extends RootApiForToken<MemberInfoResult, MemberInfoInput> {

	public MemberInfoResult ProcessQqq(MemberInfoInput inputParam, MDataMap mRequestMap) {
		JSONObject input = JSONObject.parseObject(mRequestMap.get("api_client"));
		String api_project = input.getString("os");
		MemberInfoResult result = new MemberInfoResult();
		String url = "http://hjy.api.lacues.cn/API/APIHandler.ashx";   // 测试地址
//		String url = "http://api-member.huijiayou.cn/API/APIHandler.ashx";  // 正式地址
		String responseString = "";
		try {
			responseString = WebClientSupport.upPost(url , this.getsignMap(inputParam , api_project));
		} catch (Exception e) {
		}
		if(StringUtils.isNotBlank(responseString)){
			JSONObject obj = JSONObject.parseObject(responseString);
			if(obj.getInteger("resultCode") == 1){
				result.setHeadPhoto(obj.getString("headPhoto"));
				result.setNickName(obj.getString("nickName"));  
			}else{
				result.setResultCode(934205138);
				result.setResultMessage(obj.getString("resultMessage"));
			}
		}
		return result;
	}
	
	public MDataMap getsignMap(MemberInfoInput inputParam , String api_project) {
		String md5key = "amiauhsnehnujiauhz";  // 加密使用
		
		MDataMap dataMap = new MDataMap();
		dataMap.put("api_target", "com_cmall_familyhas_api_ApiMemberInfoCf"); 
		dataMap.put("api_key", "appfamilyhas"); 
		dataMap.put("api_input", JSONObject.toJSONString(inputParam));  
		dataMap.put("api_timespan", DateUtil.getSysDateTimeString()); 
		dataMap.put("api_project", api_project);   
		dataMap.put("api_token", getOauthInfo().getAccessToken()); 
		
		StringBuffer str = new StringBuffer();
		str.append(dataMap.get("api_target"))
			 .append(dataMap.get("api_key"))
			 .append(dataMap.get("api_input"))
			 .append(dataMap.get("api_timespan"))
			 .append(md5key);
		
		dataMap.put("api_secret", HexUtil.toHexString(MD5Util.md5(str.toString())));
		return dataMap;
	}
	
	
	
	
	
	
	/**
	 * @description: 老代码作废，不再使用，仅做参考  
	 *
	 * @author Yangcl
	 * @date 2016年11月7日 下午2:48:29 
	 * @version 1.0.0.1
	 */
	public MemberInfoResult Process(MemberInfoInput inputParam, MDataMap mRequestMap) {  // Old2222222
		MemberInfoResult result = new MemberInfoResult();
		// 查出个人资料
		MDataMap minfoMap = DbUp.upTable("mc_extend_info_star").one("member_code", getUserCode(), "app_code", getManageCode());
		//若数据库中没有用户信息，则为数据库中增加用户信息
		if(null == minfoMap || StringUtils.isBlank(minfoMap.get("member_code"))){
			minfoMap = new MDataMap(); 
			MDataMap loginMap = DbUp.upTable("mc_login_info").one("member_code", getUserCode());
			minfoMap.put("member_code", loginMap.get("member_code"));
			minfoMap.put("nickname", StringUtils.substring(loginMap.get("login_name"), 0, 3) + "*****" + StringUtils.substring(loginMap.get("login_name"), 8 , 11));
			minfoMap.put("member_avatar", "");
			TxMemberForStar memberService = BeansHelper.upBean("bean_com_cmall_membercenter_txservice_TxMemberForStar");
			memberService.initExtendInfo(new MDataMap("login_name" , loginMap.get("login_name"), "manage_code",
							getManageCode(), "member_code", getUserCode(), "nick_name",
							StringUtils.substring(
									loginMap.get("login_name"), 0, 3)
									+ "*****"
									+ StringUtils.substring(
											loginMap.get("login_name"),
											8, 11)));
			
		}
		boolean flag = false;//是否需要执行修改
		
		if(StringUtils.isNotBlank(inputParam.getNickName())){//过滤验证昵称唯一及是否含有敏感词
			//验证昵称中是否包含敏感词	
			String datawhere = " SELECT * from nc_sensitive_word where sensitive_word in ("+getSubNickNameStr(inputParam.getNickName())+") ";
			List<Map<String, Object>> sensitiveList = DbUp.upTable("nc_sensitive_word").dataSqlList(datawhere, null);
			if(sensitiveList != null && sensitiveList.size() > 0){//昵称包含敏感词
				result.setResultCode(934205138);
				result.setResultMessage(bInfo(934205138));
			}else{
				// 查出当前昵称在数据库中是否存在
				MDataMap nickNameMap = DbUp.upTable("mc_extend_info_star").one("nickname", inputParam.getNickName(), "app_code", getManageCode());
				if(nickNameMap!=null && StringUtils.isNotBlank(nickNameMap.get("nickname"))){//昵称已经存在
					result.setResultCode(934205137);
					result.setResultMessage(bInfo(934205137));
				}else{
					minfoMap.put("nickname", inputParam.getNickName());//昵称
					flag = true;
				}
			}
		}
		
		if(StringUtils.isNotBlank(inputParam.getHeadPhoto())){
			minfoMap.put("member_avatar", inputParam.getHeadPhoto());//头像
			minfoMap.put("status","449746600001");//头像状态，默认正常
			flag = true;
		}

		if(flag)DbUp.upTable("mc_extend_info_star").update(minfoMap);//修改用户信息
		
		if(!"449746600002".equals(minfoMap.get("status"))){//若头像未禁用
			result.setHeadPhoto(minfoMap.get("member_avatar"));
		}
		if(StringUtils.isNotBlank(minfoMap.get("nickname")) && minfoMap.get("nickname").indexOf("*")<0){//若昵称中存在“*”为手机号
			result.setNickName(minfoMap.get("nickname"));
		}
		return result;
	}
	
	/**
	 * 获取昵称的所有子串,例:"'1','2','12'"
	 * @param nickName
	 * @return
	 */
	public String getSubNickNameStr(String nickName){
		String str = "";
		for(int i = nickName.length();i>0;i--){
			try{
				int j = 0 ;
				int z = i;
				while(true){
					str += "'"+nickName.substring(j++,z++)+"',";
				}
			}catch(Exception e){
				continue;
			}
		}
		return str.substring(0,str.length()-1);
	}

}
