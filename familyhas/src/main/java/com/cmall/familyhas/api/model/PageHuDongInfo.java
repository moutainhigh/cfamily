package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PageHuDongInfo {
	
	@ZapcomApi(value="活动编号")
	private String event_code = "";
	@ZapcomApi(value="领取积分次数")
	private String lq_times = "";
	@ZapcomApi(value="第一次领取倒计时秒数")
	private String f_time = "";
	@ZapcomApi(value="第一次领取积分数量")
	private String f_integral = "";
	@ZapcomApi(value="第二次领取倒计时秒数")
	private String s_time = "";
	@ZapcomApi(value="第二次领取积分数量")
	private String s_integral = "";
	@ZapcomApi(value="第三次领取倒计时秒数")
	private String t_time = "";
	@ZapcomApi(value="第三次领取积分数量")
	private String t_integral = "";
	@ZapcomApi(value="跳转链接")
	private String url = "";
	@ZapcomApi(value ="用户今天已领取的次数")
	private String user_lq_times = "0";
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEvent_code() {
		return event_code;
	}
	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}
	public String getLq_times() {
		return lq_times;
	}
	public void setLq_times(String lq_times) {
		this.lq_times = lq_times;
	}
	public String getF_time() {
		return f_time;
	}
	public void setF_time(String f_time) {
		this.f_time = f_time;
	}
	public String getF_integral() {
		return f_integral;
	}
	public void setF_integral(String f_integral) {
		this.f_integral = f_integral;
	}
	public String getS_time() {
		return s_time;
	}
	public void setS_time(String s_time) {
		this.s_time = s_time;
	}
	public String getS_integral() {
		return s_integral;
	}
	public void setS_integral(String s_integral) {
		this.s_integral = s_integral;
	}
	public String getT_time() {
		return t_time;
	}
	public void setT_time(String t_time) {
		this.t_time = t_time;
	}
	public String getT_integral() {
		return t_integral;
	}
	public void setT_integral(String t_integral) {
		this.t_integral = t_integral;
	}
	public String getUser_lq_times() {
		return user_lq_times;
	}
	public void setUser_lq_times(String user_lq_times) {
		this.user_lq_times = user_lq_times;
	}		
	
}
