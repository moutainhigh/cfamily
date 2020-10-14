package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
* @ClassName: BrandProduct 
* @Description: 品牌特惠关联商品
* @author 张海生
* @date 2015-5-11 下午6:08:40 
*  
*/
public class BrandProduct {
	@ZapcomApi(value="商品图片", remark="轮播图第一张")
	private String pic="";
	
	@ZapcomApi(value="商品编号")
	private String procuctCode="";
	
	@ZapcomApi(value="商品名称")
	private String productName="";
	
	@ZapcomApi(value="商品销售价")
	private BigDecimal salePrice = new BigDecimal(0);
	
	@ZapcomApi(value="市场价")
	private BigDecimal marketPrice = new BigDecimal(0);
	
	@ZapcomApi(value="折扣")
	private String discount="";
	
	@ZapcomApi(value="库存标识", remark="库存>0：有货；库存=0：售罄")
	private String storeFlag="";

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getProcuctCode() {
		return procuctCode;
	}

	public void setProcuctCode(String procuctCode) {
		this.procuctCode = procuctCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStoreFlag() {
		return storeFlag;
	}

	public void setStoreFlag(String storeFlag) {
		this.storeFlag = storeFlag;
	}

}
