package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/***
 * 翻页结果
 * @author duanyc
 * date 2014-9-17
 * @version 1.0
 */
public class PageResults {

	@ZapcomApi(value = "总数量")
	private int total = 0;
	
	@ZapcomApi(value = "返回数量")
	private int count = 0;
	
	@ZapcomApi(value = "是否还有更多")
	private int  more = 0;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMore() {
		return more;
	}

	public void setMore(int more) {
		this.more = more;
	}
	
}
