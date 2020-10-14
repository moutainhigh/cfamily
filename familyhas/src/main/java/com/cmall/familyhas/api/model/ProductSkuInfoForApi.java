package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.helper.MoneyHelper;


/**   
*    
* 项目名称：familyhas   
* 类名称：ProductSkuInfoForApi   
* 类描述：   
* 创建人：李国杰
*
* @version    
*    
*/
public class ProductSkuInfoForApi  {
    
	@ZapcomApi(value = "sku编码")
    private String skuCode  = ""  ;

	@ZapcomApi(value = "sku名称")
    private String skuName  = ""  ;
	
	@ZapcomApi(value = "sku规格")
    private String keyValue  = ""  ;

	@ZapcomApi(value = "总库存",remark="各个仓库库存之和")
    private int stockNumSum  = 0  ;
	
	@ZapcomApi(value = "销售价")
	private BigDecimal sellPrice=new BigDecimal(0.00);
	
	@ZapcomApi(value="积分")
	private BigDecimal integral;

	@ZapcomApi(value = "市场价")
	private BigDecimal marketPrice = new BigDecimal(0.00);
	
	@ZapcomApi(value="促销信息",remark="在新商品详情页已作废")
	private List<ActivitySell> activityInfo = new ArrayList<ActivitySell>();
	
	@ZapcomApi(value="内购价")
	private String vipSpecialPrice = "0";
	
	@ZapcomApi(value="返现金额",remark="返现金额，默认是售价的5%")
	private  BigDecimal disMoney=new BigDecimal(0.00);
	
	@ZapcomApi(value = "限购数",remark="暂时为促销系统限购数所用")
    private int skuMaxBuy=99;
	
	@ZapcomApi(value = "起订数量",remark="最少购买数，默认为1")
	private int miniOrder = 1;

	@ZapcomApi(value = "每用户限购数",remark="")
    private int limitBuy=99;
	
	@ZapcomApi(value = "是否显示限购数，0：不显示，1显示")
	private int showLimitNum=0;
	
	@ZapcomApi(value="购买状态", remark="状态值对应：1(允许购买),2(活动尚未开始),3(活动已结束),4(活动进行中但是不可购买),5(其他状态位)")
	private String buyStatus = "1";
	
	@ZapcomApi(value="用户最大限购个数")
	private int maxLimit;
	
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

	public int getStockNumSum() {
		return stockNumSum;
	}

	public void setStockNumSum(int stockNumSum) {
		this.stockNumSum = stockNumSum;
	}
	
	public BigDecimal getIntegral() {
		return integral;
	}

	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}

	public BigDecimal getSellPrice() {
//		return sellPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return MoneyHelper.roundHalfUp(sellPrice); // 兼容小数 - Yangcl
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getMarketPrice() {
//		return marketPrice.setScale(0, BigDecimal.ROUND_DOWN);
		return MoneyHelper.roundHalfUp(marketPrice); // 兼容小数 - Yangcl
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

	public BigDecimal getDisMoney() {
		return disMoney.setScale(2, BigDecimal.ROUND_DOWN);
	}

	public void setDisMoney(BigDecimal disMoney) {
		this.disMoney = disMoney;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}

	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
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

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}
}

