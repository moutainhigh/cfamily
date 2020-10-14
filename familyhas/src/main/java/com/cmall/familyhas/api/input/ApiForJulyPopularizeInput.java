package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @author Angel Joy
* @Time 2020年6月11日 上午10:36:40 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForJulyPopularizeInput extends RootInput {
	
	@ZapcomApi(value="当前页码",remark="1",demo="1",require = 1)
	private Integer currentPage = 1;
	
	@ZapcomApi(value="每页个数",remark="10",demo="10",require = 1)
	private Integer pageNum = 10;
	
	@ZapcomApi(value="来源区分是否是小程序",remark="xcx",demo="xcx",require = 0)
	private String source = "";

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
