package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiJoinIntegratlTeamInput;
import com.cmall.familyhas.api.result.ApiJoinIntegratlTeamResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.message.SendMessageBase;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 加入积分战队
 * @remark 
 * @author 任宏斌
 * @date 2019年3月14日
 */
public class ApiJoinIntegratlTeam extends RootApiForToken<ApiJoinIntegratlTeamResult,ApiJoinIntegratlTeamInput>{

	@Override
	public ApiJoinIntegratlTeamResult Process(ApiJoinIntegratlTeamInput inputParam, MDataMap mRequestMap) {
		ApiJoinIntegratlTeamResult result = new ApiJoinIntegratlTeamResult();
		
		String eventCode = inputParam.getEvent_code();//活动编号
		String inviterCode = inputParam.getInviter_code();//邀请人编号
		String inviteeCode = inputParam.getInvitee_code();//被邀请人编号 
		
		//非空校验
		if(StringUtils.isEmpty(inviterCode) || StringUtils.isEmpty(inviteeCode) || StringUtils.isEmpty(eventCode)) {
			result.setResultCode(916423600);
			result.setResultMessage(bInfo(916423600));
			return result;
		}
		
		//校验活动
		String query = "SELECT event_code FROM sc_hudong_event_info WHERE event_status='4497472700020002' AND "
				+ "event_code=:event_code AND begin_time<=SYSDATE() AND end_time>SYSDATE()";
		Map<String, Object> integratEvent = DbUp.upTable("sc_hudong_event_info").dataSqlOne(query, new MDataMap("event_code",eventCode));
		if(null==integratEvent) {
			result.setResultCode(916423605);
			result.setResultMessage(bInfo(916423605));
			return result;
		}
		
		//校验邀请人与被邀请人是否一样
		if(inviterCode.equals(inviteeCode)) {
			result.setResultCode(916423601);
			result.setResultMessage(bInfo(916423601));
			return result;
		}
		
		//校验两人是否已经绑定
		int flag1 = DbUp.upTable("mc_integral_relation").count("inviter_code",inviterCode,"invitee_code",inviteeCode,"is_valid","1","event_code",eventCode);
		if(flag1>0) {
			result.setResultCode(916423604);
			result.setResultMessage(bInfo(916423604));
			return result;
		}
		
		//校验邀请人的好友是否达到上限
		int count1 = DbUp.upTable("mc_integral_relation").count("inviter_code",inviterCode,"event_code",eventCode);
		if(count1>=2) {
			result.setResultCode(916423602);
			result.setResultMessage(bInfo(916423602));
			return result;
		}
		
		//校验被邀请人的好友是否达到上限
		int count2 = DbUp.upTable("mc_integral_relation").count("inviter_code",inviteeCode,"event_code",eventCode);
		if(count2>=2) {
			result.setResultCode(916423603);
			result.setResultMessage(bInfo(916423603));
			return result;
		}
		
		//绑定关系 加入战队
		String now = DateUtil.getSysDateTimeString();
		DbUp.upTable("mc_integral_relation").insert("inviter_code", inviterCode, "invitee_code", inviteeCode,
				"is_valid", "1", "is_main", "1", "create_time", now, "update_time", now,"event_code",eventCode);
		DbUp.upTable("mc_integral_relation").insert("inviter_code", inviteeCode, "invitee_code", inviterCode,
				"is_valid", "1", "is_main", "2", "create_time", now, "update_time", now,"event_code",eventCode);
		
		//发送短信
		MOauthInfo oauthInfo = getOauthInfo();
		String inviteePhone = oauthInfo.getLoginName();
		String inviterPhone = (String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",inviterCode));
		if(StringUtils.isNotEmpty(inviterPhone)) {
			SendMessageBase messageBase = new SendMessageBase();
			String content = "好友" + inviteePhone + "与您绑定，快去看看吧。";
			messageBase.sendMessage(inviterPhone, content,"4497467200020006");
		}
		return result;
	}

}
