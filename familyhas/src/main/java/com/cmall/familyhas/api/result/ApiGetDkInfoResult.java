package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.DkInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiGetDkInfoResult extends RootResultWeb {
	
	@ZapcomApi(value = "活动编号")
	private String eventCode;
	
	@ZapcomApi(value = "用户今天是否可打卡Y/N")
	private String canDk;
	
	@ZapcomApi(value="打卡奖励信息列表",remark="")
	private List<DkInfo> list= new ArrayList<DkInfo>();

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getCanDk() {
		return canDk;
	}

	public void setCanDk(String canDk) {
		this.canDk = canDk;
	}

	public List<DkInfo> getList() {
		return list;
	}

	public void setList(List<DkInfo> list) {
		this.list = list;
	}
	
	public void addInfo(DkInfo info) {
		list.add(info);
	}


}
