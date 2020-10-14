package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.LiveProd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetLiveRoomProdListResult extends RootResult {

	@ZapcomApi(value="分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value = "直播商品集合", remark = "")
	private List<LiveProd> liveProdList = new ArrayList<LiveProd>();

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<LiveProd> getLiveProdList() {
		return liveProdList;
	}

	public void setLiveProdList(List<LiveProd> liveProdList) {
		this.liveProdList = liveProdList;
	}
	
}
