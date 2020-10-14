package com.cmall.familyhas.api.input;

import com.cmall.familyhas.api.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetHotBrandInput extends RootInput {
	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
	private PageOption paging = new PageOption();
 
	/**
	 * 获取  paging
	 */
	public PageOption getPaging() {
		return paging;
	}

	/**
	 * 设置 
	 * @param paging 
	 */
	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
	
	
}
