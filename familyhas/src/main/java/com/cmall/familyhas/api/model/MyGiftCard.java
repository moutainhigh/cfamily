package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 我的礼金卷
 * @author dyc
 * date: 2014-11-20
 * @version1.0
 */
public class MyGiftCard {

//	礼金券名称		数量	有效期	来源
	@ZapcomApi(value = "礼金券名称", remark = "礼金券名称")
	private String giftCard_name = "";
	
	@ZapcomApi(value = "面值", remark = "面值")
	private String giftCard_par = "0";
	
	@ZapcomApi(value = "最低消费额", remark = "最低消费额")
	private String giftCard_min_txtPrice = "0";
	
	@ZapcomApi(value = "数量", remark = "数量")
	private String giftCard_num = "0";
	
//	@ZapcomApi(value = "有效期开始时间", remark = "有效期开始时间")
//	private String giftCard_fre_date = "";
	
	@ZapcomApi(value = "有效期结束时间/失效日期", remark = "有效期结束时间/失效日期")
	private String giftCard_end_date = "";
	
	@ZapcomApi(value = "来源", remark = "来源")
	private String giftCard_source = "";
	
	
	
	@ZapcomApi(value = "消费数量", remark = "消费数量")
	private String giftCard_txtPrice_num = "";
	
	@ZapcomApi(value = "消费订单", remark = "消费订单")
	private String giftCard_txtPrice_orderid = "";

	public String getGiftCard_name() {
		return giftCard_name;
	}

	public void setGiftCard_name(String giftCard_name) {
		this.giftCard_name = giftCard_name;
	}

	public String getGiftCard_par() {
		return giftCard_par;
	}

	public void setGiftCard_par(String giftCard_par) {
		this.giftCard_par = giftCard_par;
	}

	public String getGiftCard_min_txtPrice() {
		return giftCard_min_txtPrice;
	}

	public void setGiftCard_min_txtPrice(String giftCard_min_txtPrice) {
		this.giftCard_min_txtPrice = giftCard_min_txtPrice;
	}

	public String getGiftCard_num() {
		return giftCard_num;
	}

	public void setGiftCard_num(String giftCard_num) {
		this.giftCard_num = giftCard_num;
	}

	public String getGiftCard_end_date() {
		return giftCard_end_date;
	}

	public void setGiftCard_end_date(String giftCard_end_date) {
		this.giftCard_end_date = giftCard_end_date;
	}

	public String getGiftCard_source() {
		return giftCard_source;
	}

	public void setGiftCard_source(String giftCard_source) {
		this.giftCard_source = giftCard_source;
	}

	public String getGiftCard_txtPrice_num() {
		return giftCard_txtPrice_num;
	}

	public void setGiftCard_txtPrice_num(String giftCard_txtPrice_num) {
		this.giftCard_txtPrice_num = giftCard_txtPrice_num;
	}

	public String getGiftCard_txtPrice_orderid() {
		return giftCard_txtPrice_orderid;
	}

	public void setGiftCard_txtPrice_orderid(String giftCard_txtPrice_orderid) {
		this.giftCard_txtPrice_orderid = giftCard_txtPrice_orderid;
	}
	
	
}
