package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class SearchWords {
	@ZapcomApi(value = "搜索关键词", remark = "搜索关键词")
	private String words;
	@ZapcomApi(value = "搜索类型", remark = "449748360001:按关键字搜索,449748360002:直接跳转URL")
	private String click_type;
	@ZapcomApi(value = "跳转URL地址", remark = "click_type为449748360001时为空")
	private String click_value;
	@ZapcomApi(value = "开始时间时分秒", remark = "开始时间时分秒")
	private String begin_time;
	@ZapcomApi(value = "结束时间时分秒", remark = "结束时间时分秒")
	private String end_time;
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getClick_type() {
		return click_type;
	}
	public void setClick_type(String click_type) {
		this.click_type = click_type;
	}
	public String getClick_value() {
		return click_value;
	}
	public void setClick_value(String click_value) {
		this.click_value = click_value;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
}
