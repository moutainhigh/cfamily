package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 满减列表sku基本数据
 * 
 * @author zhouguohui
 *
 */
public class SkuGoodsDetail {

	@ZapcomApi(value = "sku编码")
	private String skuCode = "";

	@ZapcomApi(value = "sku名称")
	private String skuName = "";

	@ZapcomApi(value = "sku规格")
	private String keyValue = "";

	@ZapcomApi(value = "总库存", remark = "各个仓库库存之和    满减列表暂时没有赋值")
	private int stockNumSum = 0;

	@ZapcomApi(value = "销售价")
	private BigDecimal sellPrice = new BigDecimal(0.00);

	@ZapcomApi(value = "市场价")
	private BigDecimal marketPrice = new BigDecimal(0.00);

	@ZapcomApi(value = "促销信息")
	private List<ActivitySell> activityInfo = new ArrayList<ActivitySell>();

	@ZapcomApi(value = "内购价", remark = "满减列表暂时没有赋值")
	private String vipSpecialPrice = "0";

	@ZapcomApi(value = "返现金额", remark = "返现金额，默认是售价的5%    满减列表暂时没有赋值")
	private BigDecimal disMoney = new BigDecimal(0.00);

	@ZapcomApi(value = "限购数", remark = "暂时为促销系统限购数所用 ")
	private int skuMaxBuy = 99;

	@ZapcomApi(value = "起订数量", remark = "最少购买数，默认为1   满减列表暂时没有赋值")
	private int miniOrder = 1;

	@ZapcomApi(value = "每用户限购数", remark = "")
	private int limitBuy = 99;

	@ZapcomApi(value = "剩余促销库存")
	private long limitStock = 0;
	@ZapcomApi(value = "购买状态", remark = "购买的状态 只有该字段等于1时才可以允许购买按钮点击    状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位),6(商品下架),7(已打限购上限)", verify = "in=0,1,2,3,4,5,6")
	private int buyStatus = 0;
	@ZapcomApi(value = "是否显示限购数", remark = "0:不显示，1:显示")
	private int showLimitNum = 0;

	@ZapcomApi(value = "sku主图", remark = "")
	private String skuPic = "";

	public String getSkuPic() {
		return skuPic;
	}

	public void setSkuPic(String skuPic) {
		this.skuPic = skuPic;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public int getStockNumSum() {
		return stockNumSum;
	}

	public void setStockNumSum(int stockNumSum) {
		this.stockNumSum = stockNumSum;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public List<ActivitySell> getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(List<ActivitySell> activityInfo) {
		this.activityInfo = activityInfo;
	}

	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}

	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
	}

	public BigDecimal getDisMoney() {
		return disMoney;
	}

	public void setDisMoney(BigDecimal disMoney) {
		this.disMoney = disMoney;
	}

	public int getSkuMaxBuy() {
		return skuMaxBuy;
	}

	public void setSkuMaxBuy(int skuMaxBuy) {
		this.skuMaxBuy = skuMaxBuy;
	}

	public int getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(int miniOrder) {
		this.miniOrder = miniOrder;
	}

	public int getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(int limitBuy) {
		this.limitBuy = limitBuy;
	}

	public long getLimitStock() {
		return limitStock;
	}

	public void setLimitStock(long limitStock) {
		this.limitStock = limitStock;
	}

	public int getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
	}

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}

}
