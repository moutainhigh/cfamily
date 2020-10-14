package com.cmall.familyhas.api.model;

import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 扫码购落地页显示商品信息
 * @author 任宏斌
 */
public class ProductItem {

	@ZapcomApi(value="节目单编号", remark="1639261")
	private String formId;
	@ZapcomApi(value="内购价格", remark="内购价格，正在参加内购活动时此字段有效")
	private String vipSpecialPrice;
	@ZapcomApi(value="是否被收藏", remark="0未收藏，1已收藏")
	private int collectionProduct;
	@ZapcomApi(value="商品标签对应的图片地址")
	private String labelsPic;
	@ZapcomApi(value="商品主图图片")
	private String mainpicUrl;
	@ZapcomApi(value="商品规格")
	private List<PlusModelSkuPropertyInfo> propertyList;
	@ZapcomApi(value="权威标志")
	private List<PlusModelAuthorityLogo> authorityLogo;
	@ZapcomApi(value="商品编号")
	private String productCode;
	@ZapcomApi(value="商品sku信息")
	private List<ProductSkuInfoForApiNew> skuList;
	@ZapcomApi(value="商品名称")
	private String productName;
	@ZapcomApi(value="直播促销语")
	private String tvTips;
	@ZapcomApi(value="是否TV商品", remark="1 是，0 否")
	private int tvFlag;
	@ZapcomApi(value="优惠券编码")
	private String couponCode;
	@ZapcomApi(value="优惠券金额")
	private String couponValue;

	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponValue() {
		return couponValue;
	}
	public void setCouponValue(String couponValue) {
		this.couponValue = couponValue;
	}
	public String getVipSpecialPrice() {
		return vipSpecialPrice;
	}
	public void setVipSpecialPrice(String vipSpecialPrice) {
		this.vipSpecialPrice = vipSpecialPrice;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public int getCollectionProduct() {
		return collectionProduct;
	}
	public void setCollectionProduct(int collectionProduct) {
		this.collectionProduct = collectionProduct;
	}
	public String getLabelsPic() {
		return labelsPic;
	}
	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}
	public String getMainpicUrl() {
		return mainpicUrl;
	}
	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}
	public List<PlusModelSkuPropertyInfo> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<PlusModelSkuPropertyInfo> propertyList) {
		this.propertyList = propertyList;
	}
	public List<PlusModelAuthorityLogo> getAuthorityLogo() {
		return authorityLogo;
	}
	public void setAuthorityLogo(List<PlusModelAuthorityLogo> authorityLogo) {
		this.authorityLogo = authorityLogo;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public List<ProductSkuInfoForApiNew> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<ProductSkuInfoForApiNew> skuList) {
		this.skuList = skuList;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getTvTips() {
		return tvTips;
	}
	public void setTvTips(String tvTips) {
		this.tvTips = tvTips;
	}
	public int getTvFlag() {
		return tvFlag;
	}
	public void setTvFlag(int tvFlag) {
		this.tvFlag = tvFlag;
	}
	
}
