package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeColumnContentListInput extends RootInput {


	@ZapcomApi(value = "栏目编号", require=1,demo="COL190311100004")
	private String columnCode = "";

	@ZapcomApi(value = "用户类型", remark="4497469400050002" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value = "下一页", require=0,demo="1")
	private int nextPage = 1;
	
	@ZapcomApi(value = "每页数量", require=0,demo="6")
	private int pageSize = 12;
	
	@ZapcomApi(value = "视图类别", remark="4497471600100001:APP端,4497471600100002:微信商城,4497471600100003:PC端,4497471600100004:微信小程序 ,",require=0,demo="4497471600100004")
	private String viewType = "";

	@ZapcomApi(value="屏幕宽度",require=1,remark="用于搜索主图图片的压缩")
	private int screenWidth;
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}
	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}
	

}
