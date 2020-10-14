package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiVipExclusiveViperResult extends RootResult {
	
	@ZapcomApi(value="昵称")
	private String nickName = "";
	@ZapcomApi(value="客户等级")
	private String custLvl = "";
	@ZapcomApi(value="客户积分")
	private String custPoint = "0";
	@ZapcomApi(value="客户头像")
	private String photoUrl = "";
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getCustLvl() {
		return custLvl;
	}
	public void setCustLvl(String custLvl) {
		this.custLvl = custLvl;
	}
	
	public String getCustPoint() {
		return custPoint;
	}
	public void setCustPoint(String custPoint) {
		this.custPoint = custPoint;
	}
	
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
