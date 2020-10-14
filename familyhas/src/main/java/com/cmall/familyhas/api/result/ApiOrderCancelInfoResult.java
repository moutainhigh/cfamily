package com.cmall.familyhas.api.result;

import java.util.List;

import com.cmall.familyhas.api.input.OrderCancelInfo;
import com.cmall.familyhas.api.input.OrderReturnInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiOrderCancelInfoResult extends RootResultWeb {

	@ZapcomApi(value = "订单号")
	private String order_code = "";
	@ZapcomApi(value = "支付类型 449716200001 在线支付 ; 449716200002 货到付款 ;449716200003 积分支付; 449716200004 微信支付 ;449716200022 预付款 ;449716200024 线下支付")
	private String pay_type_code = "";
	@ZapcomApi(value = "支付类型名称")
	private String pay_type_name = "";
	@ZapcomApi(value = "支付金额")
	private String payed_money = "0.00";
	@ZapcomApi(value = "优惠券 金额")
	private String coupons_money = "0.00";
	@ZapcomApi(value = "储值金 金额")
	private String stored_value_money = "0.00";
	@ZapcomApi(value = "暂存款 金额")
	private String staginge_money = "0.00";
	@ZapcomApi(value = "积分 分数")
	private String integraly = "0";
	
	@ZapcomApi(value="惠币抵扣的钱")
	private String return_hjycoin_money;
	
	@ZapcomApi(value = "申请退款时间")
	private String applyTime;
	@ZapcomApi(value = "申请退款完成时间")
	private String completeTime;
	
	@ZapcomApi(value = "申请完成状态 0 未完成  1已完成")
	private Integer isComplete;
	
	@ZapcomApi(value = "取消列表详情")
	private List<OrderCancelInfo> cancelList;
	
	
	@ZapcomApi(value = "退款明细")
	private List<OrderReturnInfo> returnInfo;

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getPay_type_code() {
		return pay_type_code;
	}

	public void setPay_type_code(String pay_type_code) {
		this.pay_type_code = pay_type_code;
	}

	public String getPay_type_name() {
		return pay_type_name;
	}

	public void setPay_type_name(String pay_type_name) {
		this.pay_type_name = pay_type_name;
	}

	public String getPayed_money() {
		return payed_money;
	}

	public void setPayed_money(String payed_money) {
		this.payed_money = payed_money;
	}

	public String getCoupons_money() {
		return coupons_money;
	}

	public void setCoupons_money(String coupons_money) {
		this.coupons_money = coupons_money;
	}

	public String getStored_value_money() {
		return stored_value_money;
	}

	public void setStored_value_money(String stored_value_money) {
		this.stored_value_money = stored_value_money;
	}

	public String getStaginge_money() {
		return staginge_money;
	}

	public void setStaginge_money(String staginge_money) {
		this.staginge_money = staginge_money;
	}

	public String getIntegraly() {
		return integraly;
	}

	public void setIntegraly(String integraly) {
		this.integraly = integraly;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public List<OrderCancelInfo> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<OrderCancelInfo> cancelList) {
		this.cancelList = cancelList;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public List<OrderReturnInfo> getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(List<OrderReturnInfo> returnInfo) {
		this.returnInfo = returnInfo;
	}

	public String getReturn_hjycoin_money() {
		return return_hjycoin_money;
	}

	public void setReturn_hjycoin_money(String return_hjycoin_money) {
		this.return_hjycoin_money = return_hjycoin_money;
	}

	
	
	
	

	

}
