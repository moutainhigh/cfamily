package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.GoodsInfoForQueryDisable;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiQueryDisableSkuResult extends RootResult {
	@ZapcomApi(value="失效商品列表",remark="失效商品列表")
	private List<GoodsInfoForQueryDisable> loseEfficacyList = new ArrayList<GoodsInfoForQueryDisable>();

	public List<GoodsInfoForQueryDisable> getLoseEfficacyList() {
		return loseEfficacyList;
	}

	public void setLoseEfficacyList(List<GoodsInfoForQueryDisable> loseEfficacyList) {
		this.loseEfficacyList = loseEfficacyList;
	}
}