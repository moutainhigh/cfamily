package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 订单后期处理返回信息
 * @author jlin
 *
 */
public class ApiForOrderAChangeResult extends RootResultWeb {

	@ZapcomApi(value="订单号",demo="DD12646546")
	private String order_code="";
	
	@ZapcomApi(value="买家手机号",demo="13681578783")
	private String buyer_mobile="";

	@ZapcomApi(value="售后联系人",demo="毛毛")
	private String contacts="";
	
	@ZapcomApi(value="售后联系人电话",demo="13681578783")
	private String mobile="";
	
	@ZapcomApi(value="售后联系人地址",demo="北京北京")
	private String address="";
	
	@ZapcomApi(value="售后联系人邮编",demo="123456")
	private String receiver_area_code="";
	
	@ZapcomApi(value="商户编号",demo="xxxx")
	private String small_seller_code="";
	
	@ZapcomApi(value="订单金额",demo="99.99")
	private BigDecimal order_money;
	
	@ZapcomApi(value="订单状态",demo="xxxx")
	private String order_status="";
	
	@ZapcomApi(value="赋予积分",demo="2000")
	private Integer integral=0;
	
	private List<Detail> details = new ArrayList<Detail>(); 
	
	public static class Detail{
		
		@ZapcomApi(value="SKU编号",demo="15646")
		private String sku_code="";
		
		@ZapcomApi(value="商品编号",demo="15646")
		private String product_code="";
		
		@ZapcomApi(value="商品名称",demo="15646")
		private String sku_name="";
		
		@ZapcomApi(value="产品价格",demo="15646")
		private BigDecimal sku_price=BigDecimal.ZERO;
		
		@ZapcomApi(value="购买数量",demo="15646")
		private int sku_num=0;
		
		@ZapcomApi(value="已退件数",demo="15646")
		private int return_num=0;
		
		@ZapcomApi(value="退货进行中件数",demo="15646")
		private int occupy_num=0;
		
		//此处不考虑换货的情况//////////////////////////////////////////////////////////

		public String getSku_code() {
			return sku_code;
		}

		public void setSku_code(String sku_code) {
			this.sku_code = sku_code;
		}

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public BigDecimal getSku_price() {
			return sku_price;
		}

		public void setSku_price(BigDecimal sku_price) {
			this.sku_price = sku_price;
		}

		public int getSku_num() {
			return sku_num;
		}

		public void setSku_num(int sku_num) {
			this.sku_num = sku_num;
		}

		public int getReturn_num() {
			return return_num;
		}

		public void setReturn_num(int return_num) {
			this.return_num = return_num;
		}

		public int getOccupy_num() {
			return occupy_num;
		}

		public void setOccupy_num(int occupy_num) {
			this.occupy_num = occupy_num;
		}
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReceiver_area_code() {
		return receiver_area_code;
	}

	public void setReceiver_area_code(String receiver_area_code) {
		this.receiver_area_code = receiver_area_code;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public String getSmall_seller_code() {
		return small_seller_code;
	}

	public void setSmall_seller_code(String small_seller_code) {
		this.small_seller_code = small_seller_code;
	}
	
	
}
