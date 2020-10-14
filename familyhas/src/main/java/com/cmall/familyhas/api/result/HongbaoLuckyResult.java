package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class HongbaoLuckyResult extends RootResultWeb{
	
	@ZapcomApi(value="是否中奖",remark="0为未中奖，1为中奖")
	private Integer bingoFlag = 0;//0为 未中奖，1为中奖
	
	@ZapcomApi(value="中奖描述",remark="中奖描述")
	private String bingoDes = "";//中奖描述 
	
	@ZapcomApi(value="奖品类型",remark="449748120001为金额券,449748120002为折扣券")
	private String jiangpinType = "";//
	
	@ZapcomApi(value="奖品额度",remark="奖品额度")
	private Double jiangpinNum = 0.0;

	public String getJiangpinType() {
		return jiangpinType;
	}

	public void setJiangpinType(String jiangpinType) {
		this.jiangpinType = jiangpinType;
	}

	public Double getJiangpinNum() {
		return jiangpinNum;
	}

	public void setJiangpinNum(Double jiangpinNum) {
		this.jiangpinNum = jiangpinNum;
	}

	public Integer getBingoFlag() {
		return bingoFlag;
	}

	public void setBingoFlag(Integer bingoFlag) {
		this.bingoFlag = bingoFlag;
	}

	public String getBingoDes() {
		return bingoDes;
	}

	public void setBingoDes(String bingoDes) {
		this.bingoDes = bingoDes;
	}
	
	
}
