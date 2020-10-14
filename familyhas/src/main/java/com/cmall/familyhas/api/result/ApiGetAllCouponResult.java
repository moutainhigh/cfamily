package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;
import com.cmall.ordercenter.model.CouponInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 *liqt 
 */
public class ApiGetAllCouponResult extends RootResultWeb{
	
	@ZapcomApi(value="优惠劵列表",remark="优惠劵列表")
	private List<CouponInfo> couponInfoList=new ArrayList<CouponInfo>();

	@ZapcomApi(value="分页总页数",remark="分页总页数")
	private int pagination=0;
	
	@ZapcomApi(value="总条数",remark="优惠券总条数")
	private int totalCount=0;
	
	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}

	public List<CouponInfo> getCouponInfoList() {
		return couponInfoList;
	}

	public void setCouponInfoList(List<CouponInfo> couponInfoList) {
		this.couponInfoList = couponInfoList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
