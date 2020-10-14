package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForAftermarketListResult extends RootResult {

	@ZapcomApi(value = "当前页数", require = 1, demo = "1")
	private int nowPage = 0;
	
	@ZapcomApi(value = "总页数", require = 1, demo = "1")
	private int countPage = 0;

	@ZapcomApi(value = "退款/售后订单信息", require = 1, demo = "1")
	private List<AfterSale> orderList = new ArrayList<AfterSale>();
	
	public static class AfterSale {

		@ZapcomApi(value = "售后单号", require = 1, demo = "xxxxx")
		private String afterCode = "";
		
		@ZapcomApi(value = "售后单状态", require = 1, demo = "xxxxx")
		private String afterStatus = "";
		
		@ZapcomApi(value = "订单编号", require = 1, demo = "xxxxx")
		private String orderCode = "";
		
		@ZapcomApi(value = "每个订单的商品信息", require = 1, demo = "xxxxx")
		private List<Product> productList = new ArrayList<Product>();

		@ZapcomApi(value = "订单支持的功能按钮", require = 1, demo = "xxxxx")
		private List<Button> orderButtonList = new ArrayList<Button>();
		
		@ZapcomApi(value = "是否显示售后详情", require = 1,remark="'0':不显示,'1':显示", demo = "1")
		private String ifShowDetail = "1";

		
		public String getIfShowDetail() {
			return ifShowDetail;
		}

		public void setIfShowDetail(String ifShowDetail) {
			this.ifShowDetail = ifShowDetail;
		}

		public String getAfterCode() {
			return afterCode;
		}
		
		public void setAfterCode(String afterCode) {
			this.afterCode = afterCode;
		}

		public String getAfterStatus() {
			return afterStatus;
		}

		public void setAfterStatus(String afterStatus) {
			this.afterStatus = afterStatus;
		}

		public String getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}

		public List<Product> getProductList() {
			return productList;
		}

		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}

		public List<Button> getOrderButtonList() {
			return orderButtonList;
		}

		public void setOrderButtonList(List<Button> orderButtonList) {
			this.orderButtonList = orderButtonList;
		}
		
	}

	public static class Product {

		@ZapcomApi(value = "规格/款式 ", require = 1, demo = "xxxxx")
		private List<ApiSellerStandardAndStyleResult> StandardAndStyleList = new ArrayList<ApiSellerStandardAndStyleResult>();

		@ZapcomApi(value = "商品名称", require = 1, demo = "xxxxx")
		private String product_name = "";

		@ZapcomApi(value = "商品单价", require = 1, demo = "xxxxx")
		private String sell_price = "";

		@ZapcomApi(value = "商品标签", require = 1, remark = " LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品", demo = "xxxxx")
		private List<String> labelsList = new ArrayList<String>();

		@ZapcomApi(value = "商品标签对应的图片地址", require = 1, demo = "xxxxx")
		private String lablesPic = "";

		@ZapcomApi(value = "商品图片链接", require = 1, demo = "xxxxx")
		private String mainpic_url = "";

		@ZapcomApi(value = "商品编号", require = 1, demo = "xxxxx")
		private String product_code = "";

		@ZapcomApi(value = "商商品数量", require = 1, demo = "3")
		private String product_number = "";

		@ZapcomApi(value="积分")
		private String integral = "0";
		
		@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
		private String proClassifyTag="";
		
			public String getProClassifyTag() {
			return proClassifyTag;
		}
		public void setProClassifyTag(String proClassifyTag) {
			this.proClassifyTag = proClassifyTag;
		}


		
		public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
			return StandardAndStyleList;
		}

		public void setStandardAndStyleList(List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
			StandardAndStyleList = standardAndStyleList;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getSell_price() {
			return sell_price;
		}

		public void setSell_price(String sell_price) {
			this.sell_price = sell_price;
		}

		public List<String> getLabelsList() {
			return labelsList;
		}

		public void setLabelsList(List<String> labelsList) {
			this.labelsList = labelsList;
		}

		public String getLablesPic() {
			return lablesPic;
		}

		public void setLablesPic(String lablesPic) {
			this.lablesPic = lablesPic;
		}

		public String getMainpic_url() {
			return mainpic_url;
		}

		public void setMainpic_url(String mainpic_url) {
			this.mainpic_url = mainpic_url;
		}

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public String getProduct_number() {
			return product_number;
		}

		public void setProduct_number(String product_number) {
			this.product_number = product_number;
		}

		public String getIntegral() {
			return integral;
		}

		public void setIntegral(String integral) {
			this.integral = integral;
		}
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public List<AfterSale> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<AfterSale> orderList) {
		this.orderList = orderList;
	}
	
	
}
