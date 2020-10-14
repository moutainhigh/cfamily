package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 返回当前用户的昵称，头像
 * @author ligj
 * Data 2015-12-28
 */
public class MemberInfoResult extends RootResultWeb {

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
