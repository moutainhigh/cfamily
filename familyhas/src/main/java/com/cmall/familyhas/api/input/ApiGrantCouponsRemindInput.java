package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGrantCouponsRemindInput extends RootInput {

	@ZapcomApi(value="用户编号")
	private String member_code;
	
	@ZapcomApi(value="上次关闭终端时间", remark="yyyy-MM-dd HH:mm:ss", require=1)
	private String close_terminal_time;

	@ZapcomApi(value="渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003,惠家有PC订单:449747430004", require=1)
	private String channel_id;
	
	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

	public String getClose_terminal_time() {
		return close_terminal_time;
	}

	public void setClose_terminal_time(String close_terminal_time) {
		this.close_terminal_time = close_terminal_time;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	
}
