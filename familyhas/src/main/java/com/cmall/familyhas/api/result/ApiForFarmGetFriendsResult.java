package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Friend;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmGetFriendsResult extends RootResult {

	@ZapcomApi(value = "是否展示随机偷取(已偷取+可偷取好友数<10：展示)", remark = "0否 ; 1是")
	private String randomFlag = "0";
	
	@ZapcomApi(value = "随机偷取的memberCode")
	private String randomMemberCode = "";
	
	@ZapcomApi(value = "好友列表")
	private List<Friend> friendList = new ArrayList<Friend>();

	public String getRandomFlag() {
		return randomFlag;
	}

	public void setRandomFlag(String randomFlag) {
		this.randomFlag = randomFlag;
	}

	public String getRandomMemberCode() {
		return randomMemberCode;
	}

	public void setRandomMemberCode(String randomMemberCode) {
		this.randomMemberCode = randomMemberCode;
	}

	public List<Friend> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<Friend> friendList) {
		this.friendList = friendList;
	}
	
}
