package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForOrderCommentInput extends RootInput {

	@ZapcomApi(value = "屏幕宽度", remark = "屏幕宽度", demo = "500")
	private Integer screenWidth = 0;
	@ZapcomApi(value = "评价中心标签", require = 1, demo = "1 待评价、2 待晒单 、 3 已评价 ")
	private String tagType = "1";
	@ZapcomApi(value = "页码", require = 1, demo = "默认从1开始 ")
	private String pageNum = "";
	@ZapcomApi(value = "订单号", demo = " ")
	private String orderCode = "";
	@ZapcomApi(value = "SKU编码", demo = " ")
	private String skuCode = "";

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}


	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

}
