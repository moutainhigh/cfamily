package com.cmall.familyhas.service;

import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.zapcom.baseclass.BaseClass;

public class MemberLevelService extends BaseClass {

	/**
	 * 获取家有用户等级
	 * @param mobile
	 * @return
	 */
	public String upMemberLevel(String mobile) {
		PlusModelMemberLevel levelInfo = null;
		levelInfo = new LoadMemberLevel().getCustLevelInfo(mobile);
		if(levelInfo == null){
			return "10"; //默认家有客户等级： 10 顾客
		} else {
			return levelInfo.getLevel();
		}
	}
}
