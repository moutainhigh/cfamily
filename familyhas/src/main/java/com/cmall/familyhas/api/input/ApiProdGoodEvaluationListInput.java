package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiProdGoodEvaluationListInput extends RootInput {

	@ZapcomApi(value="首页定位栏目链接类型",remark="商品评价:4497471600580001",require=1)
	private String homePositionLinkType = "";
	
	@ZapcomApi(value="分页页码",remark="第几页数据,从第二页开始获取 , 第一页内容通过首页接口返回",require=1)
	private int page = 2;

	public String getHomePositionLinkType() {
		return homePositionLinkType;
	}

	public void setHomePositionLinkType(String homePositionLinkType) {
		this.homePositionLinkType = homePositionLinkType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	
}
