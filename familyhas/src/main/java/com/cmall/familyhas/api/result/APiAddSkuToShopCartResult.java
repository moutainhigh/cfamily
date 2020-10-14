package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.Activity;
import com.cmall.familyhas.model.GoodsInfoForQuery;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiAddSkuToShopCartResult extends RootResult {

	@ZapcomApi(value="购物车商品列表",remark="购物车商品列表信息")
	private List<GoodsInfoForQuery> shoppingCartList = new ArrayList<GoodsInfoForQuery>();
	
	@ZapcomApi(value="优惠头条",remark="优惠头条信息")
	private String salesAdv = "";
	
	@ZapcomApi(value="失效商品条数",remark="失效商品条数")
	private int disableSku = 0;

	@ZapcomApi(value="商品总数量",remark="有效和无效商品总数量")
	private int acount_num = 0;
	
	@ZapcomApi(value="失效商品数量",remark="无效商品数量")
	private int disable_account_num = 0;
	
	@ZapcomApi(value="满邮价格")
	private int  postal_price = 0;
	
	@ZapcomApi(value="活动",remark="活动")
	private List<Activity> activityList = new ArrayList<Activity>();
	
	public List<GoodsInfoForQuery> getShoppingCartList() {
		return shoppingCartList;
	}

	public void setShoppingCartList(List<GoodsInfoForQuery> shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}

	public String getSalesAdv() {
		return salesAdv;
	}

	public void setSalesAdv(String salesAdv) {
		this.salesAdv = salesAdv;
	}

	public int getDisableSku() {
		return disableSku;
	}

	public void setDisableSku(int disableSku) {
		this.disableSku = disableSku;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public int getAcount_num() {
		return acount_num;
	}

	public void setAcount_num(int acount_num) {
		this.acount_num = acount_num;
	}

	public int getDisable_account_num() {
		return disable_account_num;
	}

	public void setDisable_account_num(int disable_account_num) {
		this.disable_account_num = disable_account_num;
	}

	public int getPostal_price() {
		return postal_price;
	}

	public void setPostal_price(int postal_price) {
		this.postal_price = postal_price;
	}
}