package com.cmall.familyhas.api.result.ld;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class AfterSaleOrder {
	@ZapcomApi(value="zid",remark="自增id")
	private Integer zid;
	@ZapcomApi(value="uid",remark="UID")
	private String uid;
	@ZapcomApi(value="用户编码",remark="用户编码")
	private String memberCode;
	@ZapcomApi(value="商品编码",remark="商品编码")
	private String productCode;
	@ZapcomApi(value="SKU编码",remark="SKU编码")
	private String skuCode;
	@ZapcomApi(value="订单编号",remark="订单编号")
	private String orderCode;
	@ZapcomApi(value="APP售后单号",remark="APP售后单号")
	private String afterSaleCodeApp;
	@ZapcomApi(value="LD售后单号",remark="LD售后单号")
	private String afterSaleCodeLd;
	@ZapcomApi(value="退货单号",remark="退货单号")
	private String returnCode;
	@ZapcomApi(value="售后状态",remark="售后状态")
	private String afterSaleStatus;
	@ZapcomApi(value="卖家编号",remark="卖家编号")
	private String sellerCode;
	@ZapcomApi(value="创建时间",remark="创建时间")
	private String createTime;
	@ZapcomApi(value="变更时间",remark="变更时间")
	private String modifTime;
	public Integer getZid() {
		return zid;
	}
	public void setZid(Integer zid) {
		this.zid = zid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getAfterSaleCodeApp() {
		return afterSaleCodeApp;
	}
	public void setAfterSaleCodeApp(String afterSaleCodeApp) {
		this.afterSaleCodeApp = afterSaleCodeApp;
	}
	public String getAfterSaleCodeLd() {
		return afterSaleCodeLd;
	}
	public void setAfterSaleCodeLd(String afterSaleCodeLd) {
		this.afterSaleCodeLd = afterSaleCodeLd;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getAfterSaleStatus() {
		return afterSaleStatus;
	}
	public void setAfterSaleStatus(String afterSaleStatus) {
		this.afterSaleStatus = afterSaleStatus;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifTime() {
		return modifTime;
	}
	public void setModifTime(String modifTime) {
		this.modifTime = modifTime;
	}
	
	
}
