package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 订单详情输入参数
 * @author wz
 *
 */
public class ApiIntegralDetailsInput extends RootInput{
	
	@ZapcomApi(value="请求页码")
	private String pageNum="1";
	
	@ZapcomApi(value="每页显示数量")
	private String pageCount = "10";
	
	public String getPageNum() {
		return pageNum;
	}
	
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
	public String getPageCount() {
		return pageCount;
	}
	
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	
	
}
