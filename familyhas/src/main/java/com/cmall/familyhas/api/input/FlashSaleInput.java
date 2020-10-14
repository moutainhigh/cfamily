package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class FlashSaleInput extends RootInput {
	

	@ZapcomApi(value="当前展示活动编号",remark="活动编号", require=1)
	private String eventCode = "";
	
	@ZapcomApi(value="请求来源",remark="来源：(xcx:小程序)，(wxshop:微信商城)，(ios:苹果APP) ,(android:安卓APP)", require=0,demo="xcx")
	private String source = "";
	
	@ZapcomApi(value="openId",remark="", require=0)
	private String openId = "";
	
	@ZapcomApi(value = "图片宽度", remark="图片宽度" ,demo="400")
	private int imgWidth=0;
	
	@ZapcomApi(value = "当前档", remark = "1:当前档，0:即将开始")
	private String currentFlag = "";
	

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getCurrentFlag() {
		return currentFlag;
	}

	public void setCurrentFlag(String currentFlag) {
		this.currentFlag = currentFlag;
	}
	
}
