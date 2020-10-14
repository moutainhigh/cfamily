package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.BrandBaseInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 优惠券类型限制之品牌
 * @author ligj
 *
 */
public class ApiForCouponLimitBrandBaseInfoResult extends RootResultWeb {
	@ZapcomApi(value = "品牌基本信息")
	private List<BrandBaseInfo> brandInfoList = new ArrayList<BrandBaseInfo>();

	public List<BrandBaseInfo> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<BrandBaseInfo> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
}
