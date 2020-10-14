package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HuodongEventDzpjfRule {

	@ZapcomApi(value = "活动编号")
	private String eventCode;
	
	@ZapcomApi(value = "奖品编号")
	private String jpCode;
	
	@ZapcomApi(value = "奖品名称")
	private String jpTitle;
	
	@ZapcomApi(value = "奖品图片")
	private String jpImg;
	
	@ZapcomApi(value = "中奖概率")
	private int jpZjgl;
	
	@ZapcomApi(value = "奖品数量", remark = "0表示奖品抽完了")
	private int jpNum;

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getJpCode() {
		return jpCode;
	}

	public void setJpCode(String jpCode) {
		this.jpCode = jpCode;
	}

	public String getJpTitle() {
		return jpTitle;
	}

	public void setJpTitle(String jpTitle) {
		this.jpTitle = jpTitle;
	}

	public String getJpImg() {
		return jpImg;
	}

	public void setJpImg(String jpImg) {
		this.jpImg = jpImg;
	}

	public int getJpZjgl() {
		return jpZjgl;
	}

	public void setJpZjgl(int jpZjgl) {
		this.jpZjgl = jpZjgl;
	}

	public int getJpNum() {
		return jpNum;
	}

	public void setJpNum(int jpNum) {
		this.jpNum = jpNum;
	}
	
}
