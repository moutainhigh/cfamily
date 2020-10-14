package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.ProductBaseInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 优惠券类型限制之商品
 * @author ligj
 *
 */
public class ApiForCouponLimitProductBaseInfoResult extends RootResultWeb {
	@ZapcomApi(value = "商品基本信息")
	private List<ProductBaseInfo> productInfoList = new ArrayList<ProductBaseInfo>();

	public List<ProductBaseInfo> getProductInfoList() {
		return productInfoList;
	}

	public void setProductInfoList(List<ProductBaseInfo> productInfoList) {
		this.productInfoList = productInfoList;
	}
}
