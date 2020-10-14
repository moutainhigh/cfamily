package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @author Angel Joy
* @Time 2020年5月11日 下午1:15:25 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusProductsInput extends RootInput {
	@ZapcomApi(value="分页访问页码",remark="访问页码",demo="1")
	private Integer currentPage = 1;
	
	@ZapcomApi(value="每页个数",remark="每页显示个数",demo="20")
	private Integer pageNum = 20;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	
}
