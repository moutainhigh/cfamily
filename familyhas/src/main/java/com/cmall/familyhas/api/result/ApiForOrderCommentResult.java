package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductComment;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForOrderCommentResult extends RootResultWeb {

	@ZapcomApi(value = "总页数")
	private int countPage;
	@ZapcomApi(value = "当前页数")
	private int nowPage;
	@ZapcomApi(value = "商品信息")
	private List<ProductItem> productItemList = new ArrayList<ProductItem>();
	@ZapcomApi(value = "送积分总量")
	private String integral;
	@ZapcomApi(value = "提示语")
	private String tipText;
	@ZapcomApi(value = "分享到买家秀赠送积分总量")
	private String buyerShowIntegral;

	public String getBuyerShowIntegral() {
		return buyerShowIntegral;
	}

	public void setBuyerShowIntegral(String buyerShowIntegral) {
		this.buyerShowIntegral = buyerShowIntegral;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public List<ProductItem> getProductItemList() {
		return productItemList;
	}

	public void setProductItemList(List<ProductItem> productItemList) {
		this.productItemList = productItemList;
	}

	public static class ProductItem {
		@ZapcomApi(value = "订单号", remark = "")
		private String orderCode = "";
		@ZapcomApi(value = "商品编号", remark = "")
		private String productCode = "";
		@ZapcomApi(value = "SKU编号", remark = "")
		private String skuCode = "";
		@ZapcomApi(value = "商品名称", remark = "")
		private String productName = "";
		@ZapcomApi(value = "颜色", remark = "")
		private String color = "";
		@ZapcomApi(value = "款式", remark = "")
		private String style = "";
		@ZapcomApi(value = "数量", remark = "")
		private String skuNum = "";
		@ZapcomApi(value = "商品图片", remark = "")
		private String picUrl = "";
		@ZapcomApi(value="此商品是否跨境商品", remark="1:是     0:否")
		private String flagTheSea = "0";
		@ZapcomApi(value = "商品评论", remark = "")
		private List<ProductComment> productCommentList = new ArrayList<ProductComment>();

		public String getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
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

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getStyle() {
			return style;
		}

		public void setStyle(String style) {
			this.style = style;
		}

		public List<ProductComment> getProductCommentList() {
			return productCommentList;
		}

		public void setProductCommentList(List<ProductComment> productCommentList) {
			this.productCommentList = productCommentList;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getFlagTheSea() {
			return flagTheSea;
		}

		public void setFlagTheSea(String flagTheSea) {
			this.flagTheSea = flagTheSea;
		}

		public String getSkuNum() {
			return skuNum;
		}

		public void setSkuNum(String skuNum) {
			this.skuNum = skuNum;
		}

	}
}
