package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiVipExclusiveBarginInput extends RootInput {
	
	@ZapcomApi(value="页数")
	private int page = 1;
	@ZapcomApi(value="每页条数")
	private int pageCount = 10;
	@ZapcomApi(value="排序序号", remark="0: 默认, 1: 销量升序, 2: 销量降序, 3: 价格升序, 4: 价格降序")
	private String orderSeq = "";
	@ZapcomApi(value="图片最大宽度")
	private String maxWidth = "0";
	@ZapcomApi(value="图片格式")
	private String picType = "";
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public String getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
	
	public String getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
}
