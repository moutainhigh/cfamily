package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 会员修改输入类
 * @author liguojie
 * Date 2015-12-28
 */
public class MemberInfoInput  extends RootInput {

	@ZapcomApi(value = "头像",remark="头像",demo="http://....")
	private String headPhoto = "";
	
	@ZapcomApi(value = "昵称",remark="昵称唯一",demo="西瓜")
	private String nickName = "";

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
