package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import com.cmall.familyhas.api.input.ApiOrderCancelInput;
import com.cmall.familyhas.api.input.OrderCancelInfo;
import com.cmall.familyhas.api.input.OrderReturnInfo;
import com.cmall.familyhas.api.result.ApiOrderCancelInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 5.6.6--取消详情接口
 * 
 * @author wangmeng
 *
 */
public class ApiOrderCancelInfo extends RootApiForToken<ApiOrderCancelInfoResult, ApiOrderCancelInput> {

	@Override
	public ApiOrderCancelInfoResult Process(ApiOrderCancelInput inputParam, MDataMap mRequestMap) {

		String sDate = "";// 申请时间
		Boolean isMange = false;// 是否ld商品
		String ldTime = "";// ld 申请中的时间是否存在
		String completeTime = "";// 完成时间
		Integer isComplete = 0;// 是否完成
		String orderStatus = "";// 订单状态
		MDataMap lcOrderstatus = null;

		ApiOrderCancelInfoResult apiOrderCancelInfoResult = new ApiOrderCancelInfoResult();
		String orderCode = inputParam.getOrderCode();
		// 查询订单取消记录
		MDataMap orderCancelmDataMap = new MDataMap();
		orderCancelmDataMap.put("order_code", orderCode);
		MDataMap dataSqlOne = DbUp.upTable("oc_return_money").one("order_code", orderCode);
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", orderCode);
		// 如果是ld 订单或者考拉订单
		lcOrderstatus = DbUp.upTable("lc_orderstatus").one("code", orderCode, "now_status", "4497153900010008");
		if (lcOrderstatus == null) {
			lcOrderstatus = DbUp.upTable("lc_orderstatus").one("code", orderCode, "now_status", "4497153900010006");
		}
		/*
		 * if (orderInfo.get("small_seller_code").equalsIgnoreCase("SI2003") ||
		 * orderInfo.get("small_seller_code").equalsIgnoreCase("SF03WYKLPT")) {
		 * lcOrderstatus = DbUp.upTable("lc_orderstatus").one("code", orderCode,
		 * "now_status", "4497153900010008"); } else { lcOrderstatus =
		 * DbUp.upTable("lc_orderstatus").one("code", orderCode, "now_status",
		 * "4497153900010006"); }
		 */
		if (orderInfo == null || dataSqlOne == null || lcOrderstatus == null) {
			apiOrderCancelInfoResult.setResultCode(0);
			apiOrderCancelInfoResult.setResultMessage("订单不存在");
			return apiOrderCancelInfoResult;
		}
		orderStatus = lcOrderstatus.get("now_status");
		apiOrderCancelInfoResult.setOrder_code(orderCode);
		sDate = lcOrderstatus.get("create_time");// 申请时间
		// 考拉 或者ld 商品
		if (orderInfo.get("small_seller_code").equalsIgnoreCase("SI2003")
				|| orderInfo.get("small_seller_code").equalsIgnoreCase("SF03WYKLPT")) {
			isMange = true;
			if (orderInfo.get("order_status").equalsIgnoreCase("4497153900010006")) {
				MDataMap orderInfoLc = DbUp.upTable("lc_orderstatus").one("code", orderCode, "now_status",
						"4497153900010006");
				if (orderInfoLc != null) {
					ldTime = orderInfoLc.get("create_time");
					orderStatus = orderInfoLc.get("now_status");
				}
			}
		}
		// 查询退款时间 lc_return_money_status 根据退款号(return_money_code)
		if (dataSqlOne.get("status").equalsIgnoreCase("4497153900040001")) {
			//
			MDataMap returnMoneyStatus = DbUp.upTable("lc_return_money_status").one("return_money_no",
					dataSqlOne.get("return_money_code"), "status", "4497153900040001");
			if (returnMoneyStatus != null) {
				completeTime = returnMoneyStatus.get("create_time");// 完成时间
				isComplete = 1;
			}
		}
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("orderCode", orderCode);

		/*
		 * 449716200001 在线支付 449716200002 货到付款 449716200003 积分支付 449716200004 微信支付
		 * 449716200022 预付款 449716200024 线下支付
		 */

		//查询支付方式
		Map<String, Object> orderPay = DbUp.upTable("oc_order_pay").dataSqlOne("select pay_type  from oc_order_pay where pay_type in ('449746280003','449746280005','449746280014') and order_code = '"+orderCode+"' ", null);
		if(orderPay!=null) {
			apiOrderCancelInfoResult.setPay_type_code(orderPay.get("pay_type").toString());// 支付方式
		}

		
		
		apiOrderCancelInfoResult.setPayed_money(dataSqlOne.get("online_money"));// 退款金额
		// 查询是否使用优惠券
		//查询是否存在拆单退款 big_order_code

		  int count = DbUp.upTable("oc_orderinfo").count("big_order_code",orderInfo.get("big_order_code"));
		Map<String, Object> activityMap = DbUp.upTable("oc_order_activity").dataSqlOne("select sum(preferential_money) preferential_money from oc_order_activity where ticket_code != '' and order_code = '"+orderCode+"'", null);
		if (activityMap != null && !StringUtils.isEmpty(activityMap.get("preferential_money")) && count == 1) {
			apiOrderCancelInfoResult.setCoupons_money(activityMap.get("preferential_money").toString());// 优惠券
		}
		apiOrderCancelInfoResult.setStored_value_money(dataSqlOne.get("return_ppc_money"));// 储值金 金额
		apiOrderCancelInfoResult.setStaginge_money(dataSqlOne.get("return_crdt_money"));// 暂存款 金额
		apiOrderCancelInfoResult.setIntegraly(dataSqlOne.get("return_accm_money"));// 积分 分数
		apiOrderCancelInfoResult.setReturn_hjycoin_money(dataSqlOne.get("return_hjycoin_money"));
		apiOrderCancelInfoResult.setIsComplete(isComplete);// 是否完成
		apiOrderCancelInfoResult.setCancelList(getCancelList(sDate, isMange, ldTime, completeTime, orderStatus));
		apiOrderCancelInfoResult.setReturnInfo((getReturnList(apiOrderCancelInfoResult)));
		return apiOrderCancelInfoResult;
	}

	private List<OrderReturnInfo> getReturnList(ApiOrderCancelInfoResult apiOrderCancelInfoResult) {
		List<OrderReturnInfo> returnInfo = new ArrayList<>();
		// 惠币
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getReturn_hjycoin_money())
				&& !apiOrderCancelInfoResult.getReturn_hjycoin_money().equals("0.000")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			String hjycoin = apiOrderCancelInfoResult.getReturn_hjycoin_money().replace(".000", "");
			orderReturnInfo.setType("惠币");
			orderReturnInfo.setAmount(hjycoin);
			orderReturnInfo.setDescribe("(1个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}
		// 积分
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getIntegraly())
				&& !apiOrderCancelInfoResult.getIntegraly().equals("0.00")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			orderReturnInfo.setType("积分");
			String integraly = apiOrderCancelInfoResult.getIntegraly().replace(".00", "");
			Integer valueOf = Integer.valueOf(integraly)*200;
			orderReturnInfo.setAmount(valueOf.toString());
			orderReturnInfo.setDescribe("(1个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}
		// 暂存款
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getStaginge_money())
				&& !apiOrderCancelInfoResult.getStaginge_money().equals("0.00")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			orderReturnInfo.setType("暂存款");
			orderReturnInfo.setAmount("¥" + apiOrderCancelInfoResult.getStaginge_money().replace(".00", ""));
			orderReturnInfo.setDescribe("(1个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}
		// 储值金
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getStored_value_money())
				&& !apiOrderCancelInfoResult.getStored_value_money().equals("0.00")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			orderReturnInfo.setType("储值金");
			orderReturnInfo.setAmount("¥" + apiOrderCancelInfoResult.getStored_value_money().replace(".00", ""));
			orderReturnInfo.setDescribe("(1个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}
		// 优惠券
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getCoupons_money())
				&& !apiOrderCancelInfoResult.getCoupons_money().equals("0.00")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			orderReturnInfo.setType("优惠券");
			orderReturnInfo.setAmount("¥" + apiOrderCancelInfoResult.getCoupons_money().replace(".00", ""));
			orderReturnInfo.setDescribe("(1个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}
		// 支付 方式和类型
		/*
		 * 449716200001 在线支付 449716200002 货到付款 449716200003 积分支付 449716200004 微信支付
		 * 449716200022 预付款 449716200024 线下支付
		 */
		if (!StringUtils.isEmpty(apiOrderCancelInfoResult.getPayed_money())
				&& !apiOrderCancelInfoResult.getPayed_money().equals("0.00")) {
			OrderReturnInfo orderReturnInfo = new OrderReturnInfo();
			orderReturnInfo.setType("在线支付");
			if (apiOrderCancelInfoResult.getPay_type_code().equalsIgnoreCase("449746280014")) {
				orderReturnInfo.setType("银联支付");
			}
			if (apiOrderCancelInfoResult.getPay_type_code().equalsIgnoreCase("449746280003")) {
				orderReturnInfo.setType("支付宝支付");
			}
			if (apiOrderCancelInfoResult.getPay_type_code().equalsIgnoreCase("449746280005")) {
				orderReturnInfo.setType("微信支付");
			}
			orderReturnInfo.setAmount("¥" + apiOrderCancelInfoResult.getPayed_money().replace(".00", ""));
			orderReturnInfo.setDescribe("(1-3个工作日到账)");
			returnInfo.add(orderReturnInfo);
		}

		return returnInfo;
	}

	/**
	 * 
	 * @param sDate
	 *            申请时间
	 * @param isMange
	 *            是否为ld 或者考拉的商品
	 * @param ldTime
	 *            ld 申请时间
	 * @param completeTime
	 *            结束时间 orderStatus 订单状态
	 * @return
	 */
	private List<OrderCancelInfo> getCancelList(String sDate, Boolean isMange, String ldTime, String completeTime,
			String orderStatus) {
		List<OrderCancelInfo> cancelList = new ArrayList<OrderCancelInfo>();

		if (!StringUtils.isEmpty(completeTime)) {// 完成时间
			OrderCancelInfo orderCancelInfo3 = new OrderCancelInfo();
			orderCancelInfo3.setMessage("您的退款已完成，请查看退款明细");
			orderCancelInfo3.setTime(completeTime);
			cancelList.add(orderCancelInfo3);
		}

		if (isMange && orderStatus.equalsIgnoreCase("4497153900010006")) {
			OrderCancelInfo orderCancelInfo2 = new OrderCancelInfo();
			orderCancelInfo2.setMessage("正在退款审核、请耐心等待");
			orderCancelInfo2.setTime(sDate);
			if (!StringUtils.isEmpty(ldTime)) {
				orderCancelInfo2.setTime(ldTime);
			}
			cancelList.add(orderCancelInfo2);
		}

		OrderCancelInfo orderCancelInfo1 = new OrderCancelInfo();
		orderCancelInfo1.setTime(sDate);
		if (isMange) {// 是否商户商品
			orderCancelInfo1.setMessage("收到您的取消申请、正在等待审核");
		} else {
			orderCancelInfo1.setMessage("正在退款审核、请耐心等待");
		}

		cancelList.add(orderCancelInfo1);
		OrderCancelInfo orderCancelInfo = new OrderCancelInfo();// 审核开始时间
		orderCancelInfo.setTime(sDate);
		orderCancelInfo.setMessage("您的取消申请已提交");
		cancelList.add(orderCancelInfo);

		return cancelList;
	}

}
