package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 秒杀商品列表。
 * @author Angel Joy
 *
 */
public class FlashSaleProductListResult extends RootResult {
	
	@ZapcomApi(value="商品列表",remark="")
	private List<FlashSaleProduct> items = new ArrayList<FlashSaleProduct>();
	
	@ZapcomApi(value="当前列表是否已经开始抢购",remark="0：未开始，1：已开始，2：已结束")
	private String status;
	
	@ZapcomApi(value="活动开始时间",remark="")
	private String beginTime;
	
	@ZapcomApi(value="活动结束时间",remark="")
	private String endTime;
	
	@ZapcomApi(value="系统当前时间",remark="")
	private String currencyTime = DateUtil.getSysDateTimeString();

	@ZapcomApi(value="页面轮播广告图",remark="list")
	private List<AdvertisementImg> lunBoList = new ArrayList<AdvertisementImg>();
	
	public List<AdvertisementImg> getLunBoList() {
		return lunBoList;
	}

	public void setLunBoList(List<AdvertisementImg> lunBoList) {
		this.lunBoList = lunBoList;
	}

	public String getCurrencyTime() {
		return currencyTime;
	}

	public void setCurrencyTime(String currencyTime) {
		this.currencyTime = currencyTime;
	}

	public List<FlashSaleProduct> getItems() {
		return items;
	}

	public void setItems(List<FlashSaleProduct> items) {
		this.items = items;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
