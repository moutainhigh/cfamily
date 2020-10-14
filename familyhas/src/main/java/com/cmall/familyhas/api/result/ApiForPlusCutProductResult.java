package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/** 
* @author Angel Joy
* @Time 2020年5月8日 下午2:10:27 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusCutProductResult extends RootResult {
	
	@ZapcomApi(value="商品列表",remark="")
	List<PlusSaleProduct> products = new ArrayList<PlusSaleProduct>();
	
	@ZapcomApi(value="活动开始时间",remark="")
	private String beginTime;
	
	@ZapcomApi(value="活动结束时间",remark="")
	private String endTime;
	
	@ZapcomApi(value="活动编号",remark="")
	private String eventCode;
	
	@ZapcomApi(value="橙意卡编号",remark="")
	private String plusProductCode;
	
	@ZapcomApi(value="橙意卡SKU编号",remark="")
	private String plusSkuCode;

	
	public String getPlusProductCode() {
		return plusProductCode;
	}

	public void setPlusProductCode(String plusProductCode) {
		this.plusProductCode = plusProductCode;
	}

	public String getPlusSkuCode() {
		return plusSkuCode;
	}

	public void setPlusSkuCode(String plusSkuCode) {
		this.plusSkuCode = plusSkuCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public List<PlusSaleProduct> getProducts() {
		return products;
	}

	public void setProducts(List<PlusSaleProduct> products) {
		this.products = products;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
