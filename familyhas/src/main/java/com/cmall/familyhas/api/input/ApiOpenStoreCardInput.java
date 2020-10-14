package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 储值卡开卡入参
 * @remark 
 * @author 任宏斌
 * @date 2019年3月12日
 */
public class ApiOpenStoreCardInput extends RootInput {

	@ZapcomApi(value = "买家编号", remark = "可为空，默认取当前登录人的编号", demo = "123456")
	private String buyer_code = "";
	
	@ZapcomApi(value = "卡号", remark = "卡号",require=1, demo = "")
	private String card_nm = "";
	
	@ZapcomApi(value = "卡密", remark = "卡密",require=1, demo = "")
	private String card_pwd = "";

	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

	public String getCard_nm() {
		return card_nm;
	}

	public void setCard_nm(String card_nm) {
		this.card_nm = card_nm;
	}

	public String getCard_pwd() {
		return card_pwd;
	}

	public void setCard_pwd(String card_pwd) {
		this.card_pwd = card_pwd;
	}
	
}
