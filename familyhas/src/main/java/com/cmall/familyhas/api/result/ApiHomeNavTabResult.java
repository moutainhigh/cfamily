package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeNav;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeNavTabResult extends RootResult {
	
	@ZapcomApi(value="导航项列表",remark="")
	private List<HomeNav> homeNavList = new ArrayList<HomeNav>();
	
	@ZapcomApi(value="首页滚动提示栏开关", remark="0:关闭; 1:开启;")
	private String scrollSwitch = "";

	public List<HomeNav> getHomeNavList() {
		return homeNavList;
	}

	public void setHomeNavList(List<HomeNav> homeNavList) {
		this.homeNavList = homeNavList;
	}

	public String getScrollSwitch() {
		return scrollSwitch;
	}

	public void setScrollSwitch(String scrollSwitch) {
		this.scrollSwitch = scrollSwitch;
	}
	
}
