package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiIntegratlTeamDetailsInput extends RootInput {

	@ZapcomApi(value="邀请人编号",remark="邀请人编号','号拼接", require = 1)
	private String inviter_code = "";
	@ZapcomApi(value="被邀请人编号",remark="用户进入详情页时member_code放在此字段中")
	private String invitee_code = "";
	
	public String getInviter_code() {
		return inviter_code;
	}
	public void setInviter_code(String inviter_code) {
		this.inviter_code = inviter_code;
	}
	public String getInvitee_code() {
		return invitee_code;
	}
	public void setInvitee_code(String invitee_code) {
		this.invitee_code = invitee_code;
	}
	
}
