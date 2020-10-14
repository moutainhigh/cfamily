package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HuodongEventJl {

	@ZapcomApi(value="抽奖编码")
	private String  jpCodeSeq;
	
	@ZapcomApi(value="奖品编号")
	private String  jpCode;
	
	@ZapcomApi(value="奖品名称")
	private String  jpTitle;
	
	@ZapcomApi(value="奖品类型(商品、积分、优惠券、谢谢参与)", remark = "4497471600470001:实物商品;4497471600470002:积分;4497471600470003:优惠券;4497471600470004:谢谢参与")
	private String  jpType;
	
	@ZapcomApi(value="是否发放", remark ="Y:已发放; N未发放")
	private String  ljYn;
	
	@ZapcomApi(value="中奖时间")
	private String  zjTime;

	public String getJpCodeSeq() {
		return jpCodeSeq;
	}

	public void setJpCodeSeq(String jpCodeSeq) {
		this.jpCodeSeq = jpCodeSeq;
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

	public String getJpType() {
		return jpType;
	}

	public void setJpType(String jpType) {
		this.jpType = jpType;
	}

	public String getLjYn() {
		return ljYn;
	}

	public void setLjYn(String ljYn) {
		this.ljYn = ljYn;
	}

	public String getZjTime() {
		return zjTime;
	}

	public void setZjTime(String zjTime) {
		this.zjTime = zjTime;
	}
	
	
}
