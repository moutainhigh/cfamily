package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasproduct.model.ProductActivity;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 
 * 项目名称：familyhas 类名称：ProductSkuInfoForApi 类描述： 创建人：李国杰
 *
 * @version
 * 
 */
public class ProductSkuInfoForApiNew {

	@ZapcomApi(value = "销售价")
	private BigDecimal sellPrice = new BigDecimal(0.00);

	@ZapcomApi(value = "内购价")
	private String vipSpecialPrice = "0";

	@ZapcomApi(value = "市场价")
	private BigDecimal marketPrice = new BigDecimal(0.00);

	@ZapcomApi(value = "起订数量", remark = "最少购买数，默认为1")
	private int miniOrder = 1;

	@ZapcomApi(value = "商品活动信息", remark = "sku上活动的合集")
	private List<ProductActivity> events=new ArrayList<ProductActivity>();

	@ZapcomApi(value = "sku名称")
	private String skuName = "";

	@ZapcomApi(value = "限购数", remark = "暂时为促销系统限购数所用")
	private int skuMaxBuy = 99;

	@ZapcomApi(value = "sku编码")
	private String skuCode = "";

	@ZapcomApi(value = "每用户限购数", remark = "")
	private int limitBuy = 99;

	@ZapcomApi(value = "是否可售")
	private int buyStatus = 0;

	@ZapcomApi(value = "sku规格")
	private String keyValue = "";

	@ZapcomApi(value = "总库存", remark = "各个仓库库存之和")
	private int stockNumSum = 0;

	@ZapcomApi(value = "sku图形")
	private String skuPicUrl = "";

	public BigDecimal getSellPrice() {
		// return sellPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return MoneyHelper.roundHalfUp(sellPrice); // 兼容小数 - Yangcl
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getMarketPrice() {
		// return marketPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return marketPrice == null ? null : MoneyHelper.roundHalfUp(marketPrice); // 兼容小数 - Yangcl
	}

	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}

	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
	}

	public int getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(int miniOrder) {
		this.miniOrder = miniOrder;
	}

	public List<ProductActivity> getEvents() {
		return events;
	}

	public void setEvents(List<ProductActivity> events) {
		this.events = events;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public int getSkuMaxBuy() {
		return skuMaxBuy;
	}

	public void setSkuMaxBuy(int skuMaxBuy) {
		this.skuMaxBuy = skuMaxBuy;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public int getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(int limitBuy) {
		this.limitBuy = limitBuy;
	}

	public int getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(int buyStatus) {
		this.buyStatus = buyStatus;
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

	public String getSkuPicUrl() {
		return skuPicUrl;
	}

	public void setSkuPicUrl(String skuPicUrl) {
		this.skuPicUrl = skuPicUrl;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

}
