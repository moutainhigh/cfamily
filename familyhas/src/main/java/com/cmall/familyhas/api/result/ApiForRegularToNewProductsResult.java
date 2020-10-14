package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForRegularToNewProductsResult extends RootResultWeb {

	@ZapcomApi(value="商品列表",remark="")
	private List<RegularToNewProduct> items = new ArrayList<RegularToNewProduct>();
	
	@ZapcomApi(value="活动开始时间",remark="")
	private String beginTime;
	
	@ZapcomApi(value="活动编号",remark="")
	private String eventCode;
	
	@ZapcomApi(value="活动结束时间",remark="")
	private String endTime;
	
	@ZapcomApi(value="活动说明",remark="")
	private String eventDesc;
	
	@ZapcomApi(value="系统当前时间",remark="")
	private String currencyTime = DateUtil.getSysDateTimeString();

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public List<RegularToNewProduct> getItems() {
		return items;
	}

	public void setItems(List<RegularToNewProduct> items) {
		this.items = items;
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

	public String getCurrencyTime() {
		return currencyTime;
	}

	public void setCurrencyTime(String currencyTime) {
		this.currencyTime = currencyTime;
	}
	
	
}
