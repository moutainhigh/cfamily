package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiGetMyCollectionProductInput extends RootInput {
	@ZapcomApi(value = "第几页",remark = "输入页码，,从1开始为第一页" ,demo= "1",require = 1)
	private int pageNum=1;

	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "app版本", remark = "app版本")
	private String app_vision = "";
	
	@ZapcomApi(value = "是否显示降价商品", remark = "全部:0，降价:1")
	private Integer isReduce = 0;
	
	
	
	public Integer getIsReduce() {
		return isReduce;
	}

	public void setIsReduce(Integer isReduce) {
		this.isReduce = isReduce;
	}

	public String getApp_vision() {
		return app_vision;
	}

	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}

	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
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
	
	
}
