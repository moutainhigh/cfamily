package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiIntegratlTeamDetailsInput;
import com.cmall.familyhas.api.model.TeamMember;
import com.cmall.familyhas.api.result.ApiIntegratlTeamDetailsResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分战队详情页接口
 * @remark 
 * @author 任宏斌
 * @date 2019年3月14日
 */
public class ApiIntegratlTeamDetails extends RootApiForToken<ApiIntegratlTeamDetailsResult,ApiIntegratlTeamDetailsInput>{

	@Override
	public ApiIntegratlTeamDetailsResult Process(ApiIntegratlTeamDetailsInput inputParam, MDataMap mRequestMap) {

		ApiIntegratlTeamDetailsResult result = new ApiIntegratlTeamDetailsResult();
		
		String inviterCode = inputParam.getInviter_code();//邀请人编号
		String inviteeCode = inputParam.getInvitee_code();//被邀请人编号
		
		//先判断当前是否存在积分共享活动
		String query = "SELECT event_code,begin_time,end_time FROM sc_hudong_event_info WHERE event_status='4497472700020002' AND "
				+ "event_type_code='449748210003' AND begin_time<=SYSDATE() AND end_time>SYSDATE()";
		Map<String, Object> integratEvent = DbUp.upTable("sc_hudong_event_info").dataSqlOne(query, new MDataMap());
		if(null!=integratEvent && StringUtils.isNotEmpty(integratEvent.get("event_code")+"")) {
			result.setButtionStatus("1");
			result.setEventCode(integratEvent.get("event_code")+"");
			result.setStartTime(integratEvent.get("begin_time")+"");
			result.setEndTime(integratEvent.get("end_time")+"");
		}else {
			result.setButtionStatus("0");
		}
		
		
		if(StringUtils.isNotEmpty(inviterCode) && StringUtils.isNotEmpty(inviteeCode)) {
			//加入战队
			result.setButtonType("449748330002");
		} else if(StringUtils.isNotEmpty(inviterCode)) {
			//进入详情页
			if("1".equals(result.getButtionStatus())) {
				List<TeamMember> teamList = new ArrayList<TeamMember>();
				
				String sWhere1 = "inviter_code=:inviter_code and is_valid=1 and event_code=:event_code";
				List<MDataMap> teamCodeList = DbUp.upTable("mc_integral_relation").queryAll("invitee_code", "update_time", sWhere1, new MDataMap("inviter_code",inviterCode,"event_code",integratEvent.get("event_code")+""));
				
				for (MDataMap teamCode : teamCodeList) {
					String invitee_code = teamCode.get("invitee_code");
					String sSql = "select member_code,avatar,nickname,login_name from mc_member_sync where member_code=:member_code";
					Map<String, Object> teamer = DbUp.upTable("mc_member_sync").dataSqlOne(sSql, new MDataMap("member_code",invitee_code));
					TeamMember teamMember = new TeamMember();
					teamMember.setMember_code(teamer.get("member_code")+"");
					teamMember.setAvatar(teamer.get("avatar")+"");
					if(StringUtils.isEmpty(teamer.get("nickname")+"")) {
						String login_name = teamer.get("login_name")+"";
						teamMember.setNickname(login_name.substring(0,3)+"****"+login_name.substring(7,11));
					}else {
						teamMember.setNickname(teamer.get("nickname")+"");
					}
					teamList.add(teamMember);
				}
				result.setTeamList(teamList);
			}
			
			result.setButtonType("449748330001");
		}
		
		return result;
	}

}
