package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HuodongEventDzpjpRule {

	@ZapcomApi(value = "活动编号")
	private String eventCode;
	
	@ZapcomApi(value = "奖品编号")
	private String jpCode;
	
	@ZapcomApi(value = "奖品类型(商品、积分、优惠券、谢谢参与)", remark = "4497471600470001:实物商品;4497471600470002:积分;4497471600470003:优惠券;4497471600470004:谢谢参与")
	private String jpType;
	
	@ZapcomApi(value = "优惠劵类型编号")
	private String couponTypeCode;
	
	@ZapcomApi(value = "积分数", remark = "奖品类型不是积分时值为0")
	private int jfNum;
	
	@ZapcomApi(value = "商品名称", remark = "只有实物商品有值")
	private String productName;
	
	@ZapcomApi(value = "奖品名称")
	private String jpTitle;
	
	@ZapcomApi(value = "奖品图片")
	private String jpImg;
	
	@ZapcomApi(value = "奖品数量", remark = "0表示奖品抽完了")
	private int jpNum;
	
	@ZapcomApi(value = "中奖概率")
	private int jpZjgl;
	

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

	public String getJpType() {
		return jpType;
	}

	public void setJpType(String jpType) {
		this.jpType = jpType;
	}


	public String getCouponTypeCode() {
		return couponTypeCode;
	}

	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}

	public int getJfNum() {
		return jfNum;
	}

	public void setJfNum(int jfNum) {
		this.jfNum = jfNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public int getJpNum() {
		return jpNum;
	}

	public void setJpNum(int jpNum) {
		this.jpNum = jpNum;
	}

	public int getJpZjgl() {
		return jpZjgl;
	}

	public void setJpZjgl(int jpZjgl) {
		this.jpZjgl = jpZjgl;
	}

	
}
