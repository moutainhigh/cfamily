package com.cmall.familyhas.api.input;


import com.cmall.groupcenter.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForMaybeLoveInput extends RootInput{
	//@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
	//private PageOption paging = new PageOption();
	@ZapcomApi(value = "第几页",remark = "输入页码，,从1开始为第一页" ,demo= "5",require = 1)
	private int pageNum=1;
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value="流水号")
	private String SwiftNumber="";
	
	@ZapcomApi(value="图片宽度")
	private int picWidth=0;
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getSwiftNumber() {
		return SwiftNumber;
	}

	public void setSwiftNumber(String swiftNumber) {
		SwiftNumber = swiftNumber;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public int getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}
	
	
}
