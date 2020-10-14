package com.cmall.familyhas.api.result;

import com.cmall.familyhas.api.model.PlusInfo;
import com.cmall.familyhas.api.model.PlusMemberInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/** 
 * 用户橙意卡中心返货结果类
* @author Angel Joy
* @Time 2020年6月28日 下午2:09:24 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusCenterResult extends RootResult {
	
	@ZapcomApi(value="用户相关信息",remark="用户相关信息")
	private PlusMemberInfo memberInfo = new PlusMemberInfo();
	
	@ZapcomApi(value="PLUS相关信息",remark="PLUS相关信息")
	private PlusInfo plusInfo = new PlusInfo();

	public PlusMemberInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(PlusMemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}

	public PlusInfo getPlusInfo() {
		return plusInfo;
	}

	public void setPlusInfo(PlusInfo plusInfo) {
		this.plusInfo = plusInfo;
	}
	
}

