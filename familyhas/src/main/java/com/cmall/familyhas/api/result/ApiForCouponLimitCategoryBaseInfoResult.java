package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.CategoryBaseInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 优惠券类型限制分类
 * @author ligj
 *
 */
public class ApiForCouponLimitCategoryBaseInfoResult extends RootResultWeb {
	@ZapcomApi(value = "分类信息")
	private List<CategoryBaseInfo> categoryInfoList = new ArrayList<CategoryBaseInfo>();

	public List<CategoryBaseInfo> getCategoryInfoList() {
		return categoryInfoList;
	}

	public void setCategoryInfoList(List<CategoryBaseInfo> categoryInfoList) {
		this.categoryInfoList = categoryInfoList;
	}
}
