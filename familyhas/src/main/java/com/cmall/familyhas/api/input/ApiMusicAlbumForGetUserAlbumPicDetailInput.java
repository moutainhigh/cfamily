package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiMusicAlbumForGetUserAlbumPicDetailInput extends RootInput {

	@ZapcomApi(value = "openId")
	private String openId = "";
	@ZapcomApi(value = "pageSize",require=1)
	int pageSize=6;
	@ZapcomApi(value = "nextPage",require=1)
	int nextPage=1;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
