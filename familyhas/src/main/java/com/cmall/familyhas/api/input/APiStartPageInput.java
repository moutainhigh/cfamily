package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.NavigationVersion;
import com.cmall.systemcenter.model.ClientInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiStartPageInput extends RootInput {

	@ZapcomApi(value = "推送类型", remark = "推送类型", demo = "11")
	private String pushType = "";
	
	@ZapcomApi(value = "推送token", remark="推送token" ,demo="11")
	private String pushToken = "";

	@ZapcomApi(value = "操作流水号", remark="操作流水号" ,demo="LSH0001")
	private String sqNum = "";
	
	@ZapcomApi(value = "启动页图片序号", remark="启动页图片序号" ,demo="XH0001")
	private String picNm = "";
	
	@ZapcomApi(value = "终端信息", remark="终端信息" ,demo="11")
	private ClientInfo client =  new ClientInfo();

	@ZapcomApi(value = "加密类型", remark="值为空时不加密" ,demo="des3")
	private String secret = "";
	
	@ZapcomApi(value="图片宽度",remark="图片压缩时用")
	private int width=0;
	
	@ZapcomApi(value ="导航图片版本号")
	private NavigationVersion navigationVersion = new NavigationVersion();

	
	public NavigationVersion getNavigationVersion() {
		return navigationVersion;
	}

	public void setNavigationVersion(NavigationVersion navigationVersion) {
		this.navigationVersion = navigationVersion;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getSqNum() {
		return sqNum;
	}

	public void setSqNum(String sqNum) {
		this.sqNum = sqNum;
	}

	public String getPicNm() {
		return picNm;
	}

	public void setPicNm(String picNm) {
		this.picNm = picNm;
	}

	public ClientInfo getClient() {
		return client;
	}

	public void setClient(ClientInfo client) {
		this.client = client;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
