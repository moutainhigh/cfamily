package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/***
 * 查看仓库库存状态 输出参数
 * @author jlin
 *
 */
public class ApiForGetStockNumByStoreResult extends RootResultWeb {

	@ZapcomApi(value = "库存信息", demo = "缺货")
	private String info = "";
	
	@ZapcomApi(value = "库存数量", demo = "23")
	private int stock_num =0;
	
	@ZapcomApi(value = "库存价格", demo = "5.23")
	private String sell_price = "0";
	
	@ZapcomApi(value = "返现金额", demo = "5.23",remark="返现金额为库存价格的 5% ")
	private String back_price = "0";
	
	@ZapcomApi(value = "促销信息", demo = "")
	private List<Promotion> promotionList = new ArrayList<Promotion>();
	
	
	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public int getStock_num() {
		return stock_num;
	}


	public void setStock_num(int stock_num) {
		this.stock_num = stock_num;
	}


	public String getSell_price() {
		return sell_price;
	}


	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}


	public String getBack_price() {
		return back_price;
	}


	public void setBack_price(String back_price) {
		this.back_price = back_price;
	}


	public List<Promotion> getPromotionList() {
		return promotionList;
	}


	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
	}

	/**
	 * 促销信息
	 * @author jlin
	 *
	 */
	public static class Promotion {
		
		@ZapcomApi(value = "促销名称", demo = "限时闪购")
		private String name;
		
		@ZapcomApi(value = "促销描述", demo = "限时闪购限时闪购限时闪购限时闪购")
		private String remark;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}
}
