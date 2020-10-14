package com.cmall.familyhas.api.input;

import com.cmall.groupcenter.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForAnnounceInput extends RootInput{
	
	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
	private PageOption paging = new PageOption();
	@ZapcomApi(value = "公告uid",remark = "公告uid" ,demo= "54c67e28e13342b8a774c7178fbe0e74")
	private String uid="";
	
	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
