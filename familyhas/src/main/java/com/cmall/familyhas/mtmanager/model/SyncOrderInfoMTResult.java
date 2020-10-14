package com.cmall.familyhas.mtmanager.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 
 * 同步订单
 * @author jlin
 *
 */
public class SyncOrderInfoMTResult extends RootResultWeb {

	@ZapcomApi(value = "订单信息列表")
	private List<OrderInfo> orderList=new ArrayList<SyncOrderInfoMTResult.OrderInfo>();
	
	
	public List<OrderInfo> getOrderList() {
		return orderList;
	}


	public void setOrderList(List<OrderInfo> orderList) {
		this.orderList = orderList;
	}


	public static class OrderInfo {
		
		@ZapcomApi(value = "订单编号")
		private String order_code = "";
		
		@ZapcomApi(value = "买家编号")
		private String buyer_code = "";
		
		@ZapcomApi(value = "订单金额")
		private BigDecimal order_money=BigDecimal.ZERO;
		
		@ZapcomApi(value = "订单状态",remark="4497153900010003 已发货,4497153900010004 已收货,4497153900010005 交易成功")
		private String order_status = "";
		
		@ZapcomApi(value = "状态更新时间")
		private String update_time = "";
		
		@ZapcomApi(value = "订单来源",remark="449715190002 android订单,449715190001 正常订单,449715190003 ios订单,449715190004 网站手机订单,449715190005 网站订单,449715190006 微信订单,449715190007 扫码购订单,449715190008 客服订单,449715190009 分销订单")
		private String order_source = "";
		
		@ZapcomApi(value = "付款方式",remark="449746280001 礼品卡,449746280002 优惠券,449746280003 支付宝支付,449746280004 快钱支付,449746280005 微信支付,449746280006 储值金,449746280007 暂存款,449746280008 积分,449746280009 微公社支付")
		private String pay_type = "";
		
		@ZapcomApi(value = "创建时间")
		private String create_time = "";
		
		@ZapcomApi(value = "应付款")
		private BigDecimal due_money=BigDecimal.ZERO;

		public String getOrder_code() {
			return order_code;
		}

		public void setOrder_code(String order_code) {
			this.order_code = order_code;
		}

		public String getBuyer_code() {
			return buyer_code;
		}

		public void setBuyer_code(String buyer_code) {
			this.buyer_code = buyer_code;
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

		public String getUpdate_time() {
			return update_time;
		}

		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}

		public String getOrder_source() {
			return order_source;
		}

		public void setOrder_source(String order_source) {
			this.order_source = order_source;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public BigDecimal getDue_money() {
			return due_money;
		}

		public void setDue_money(BigDecimal due_money) {
			this.due_money = due_money;
		}
		
	}
}
