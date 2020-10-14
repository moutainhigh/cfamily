package com.cmall.familyhas.api.model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 商品信息类
 * @author 李国杰
 * date 2014-09-21
 * @version 1.0
 */
public class SaleProduct {
	
//	@ZapcomApi(value = "商品编号")
//	private String skuCode = ""  ;
//
//	@ZapcomApi(value = "商品名称")
//	private String skuName = "" ;

	@ZapcomApi(value = "商品编号")
	private String productCode = ""  ;

	@ZapcomApi(value = "商品名称")
	private String productName = "" ;
	
	@ZapcomApi(value = "市场价") 
	private BigDecimal marketPrice = new BigDecimal(0.00);

	@ZapcomApi(value = "销售价") 
	private BigDecimal sellPrice = new BigDecimal(0.00);
	
	@ZapcomApi(value = "卖出的数量") 
	private int sellCount = 0;
	
	@ZapcomApi(value = "会员价格",remark="暂时默认为销售价。") 
	private BigDecimal vipuserPrice = new BigDecimal(0.00);
	
	@ZapcomApi(value = "是否有视频",remark="0:无；1:有；暂时默认为0，不设置值。") 
	private int isVideo = 0;

//	@ZapcomApi(value = "图片")
//	private String skuPicurl = "" ;
	
	@ZapcomApi(value = "图片")
	private String mainpicUrl = "" ;

	@ZapcomApi(value = "类别编号")
	private String categoryCode = "" ;
	
	@ZapcomApi(value = "商品标签")
	private List<String> labels = new ArrayList<String>();


	public String getProductCode() {
		return productCode;
	}

	public BigDecimal getMarketPrice() {
//		return marketPrice.setScale(0, BigDecimal.ROUND_FLOOR);
		return MoneyHelper.roundHalfUp(marketPrice); // 兼容小数 - Yangcl
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getSellPrice() {
//		return sellPrice.setScale(0, BigDecimal.ROUND_FLOOR);
		return MoneyHelper.roundHalfUp(sellPrice); // 兼容小数 - Yangcl
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSkuPicurl() {
		return mainpicUrl;
	}

	public int getSellCount() {
		return sellCount;
	}

	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}

	public BigDecimal getVipuserPrice() {
//		return vipuserPrice.setScale(0, BigDecimal.ROUND_FLOOR);
		return MoneyHelper.roundHalfUp(vipuserPrice); // 兼容小数 - Yangcl
	}

	public void setVipuserPrice(BigDecimal vipuserPrice) {
		this.vipuserPrice = vipuserPrice;
	}

	public String getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public void setSkuPicurl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public int getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(int isVideo) {
		this.isVideo = isVideo;
	}
	

}
