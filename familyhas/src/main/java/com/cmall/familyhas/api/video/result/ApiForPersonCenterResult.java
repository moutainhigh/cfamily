package com.cmall.familyhas.api.video.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForPersonCenterResult extends RootResult {

	@ZapcomApi(value="昵称")
	private String nickName="";	
	
	@ZapcomApi(value="头像")
	private String headPic="";	
	
	@ZapcomApi(value="获赞数")
	private String praiseNum;	
	
	@ZapcomApi(value="粉丝数")
	private String fansNum;	
	
	@ZapcomApi(value="关注数")
	private String careNum;	
	
	@ZapcomApi(value="访问数")
	private String visitNum;
	
	@ZapcomApi(value="当前用户是否关注此个人中心")
	private String isCare="N";

	public String getIsCare() {
		return isCare;
	}

	public void setIsCare(String isCare) {
		this.isCare = isCare;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(String praiseNum) {
		this.praiseNum = praiseNum;
	}

	public String getFansNum() {
		return fansNum;
	}

	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}

	public String getCareNum() {
		return careNum;
	}

	public void setCareNum(String careNum) {
		this.careNum = careNum;
	}

	public String getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(String visitNum) {
		this.visitNum = visitNum;
	}	
	
	
}
