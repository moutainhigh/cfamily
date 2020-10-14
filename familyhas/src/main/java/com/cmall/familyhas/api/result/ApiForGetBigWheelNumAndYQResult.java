package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.MemberSync;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetBigWheelNumAndYQResult extends RootResult {

	
	@ZapcomApi(value = "被邀请人信息")
	private List<MemberSync> list = new ArrayList<MemberSync>();
	
	@ZapcomApi(value = "剩余抽奖次数")
	private int remainDrawNum = 0;

	public List<MemberSync> getList() {
		return list;
	}

	public void setList(List<MemberSync> list) {
		this.list = list;
	}

	public int getRemainDrawNum() {
		return remainDrawNum;
	}

	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}
	
}
