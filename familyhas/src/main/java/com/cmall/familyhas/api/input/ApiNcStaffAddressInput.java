package com.cmall.familyhas.api.input;

import com.cmall.familyhas.api.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiNcStaffAddressInput extends RootInput{
//	@ZapcomApi(value="区分当前人地址 和 员工内部地址字段",remark="0: 当前人地址   1: 员工内部地址",require=1)
//	private String flag="0";
//	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
//	private PageOption paging = new PageOption();
	@ZapcomApi(value="显示第几页信息",remark="默认为1")
	private int nextPage = 1;

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
	
	
//	public String getFlag() {
//		return flag;
//	}
//
//	public void setFlag(String flag) {
//		this.flag = flag;
//	}

//	public PageOption getPaging() {
//		return paging;
//	}
//
//	public void setPaging(PageOption paging) {
//		this.paging = paging;
//	}
	
	
}
