package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HotBrandInfo;
import com.cmall.familyhas.api.model.PageResults;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetHotBrandResult extends RootResult {
	@ZapcomApi(value = "在售商品")
	private List<HotBrandInfo> brands = new ArrayList<HotBrandInfo>();

	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();

	/**
	 * 获取  brands
	 */
	public List<HotBrandInfo> getBrands() {
		return brands;
	}

	/**
	 * 设置 
	 * @param brands 
	 */
	public void setBrands(List<HotBrandInfo> brands) {
		this.brands = brands;
	}

	/**
	 * 获取  paged
	 */
	public PageResults getPaged() {
		return paged;
	}

	/**
	 * 设置 
	 * @param paged 
	 */
	public void setPaged(PageResults paged) {
		this.paged = paged;
	}
	
	
}
