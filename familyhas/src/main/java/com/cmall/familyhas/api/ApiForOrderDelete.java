package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.input.ApiForOrderDeleteInput;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.service.OrderService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;


/** 
* @ClassName: ApiForOrderDelete 
* @Description: 删除订单
* @author 张海生
* @date 2015-8-12 下午6:03:31 
*  
*/
public class ApiForOrderDelete extends RootApiForToken<RootResultWeb, ApiForOrderDeleteInput> {
	
	/**
	 * 以下两种订单状态支持删除   
	 * fq ++
	 */
	private static String successedOrderStatus = "4497153900010005";//订单状态未交易失败的
	private static String failedOrderStatus = "4497153900010006";//交易失败的订单

	/**
	 * 重写删除订单逻辑  fq++
	 * 以下两种订单状态支持删除：
	 * 		1、交易成功的订单
	 * 		2、交易失败的订单
	 */
	public RootResultWeb Process(ApiForOrderDeleteInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String orderCode = inputParam.getOrderCode();
		String sellerCode = getManageCode();
		
		List<String> deleteOrder = new ArrayList<String>();//需要删除的订单
		List<String> errorOrder = new ArrayList<String>();//订单状态不符合的订单
		
		
		if(orderCode.startsWith("OS")){//以OS开头的是大订单编号     （按大单号删除的订单）
			List<MDataMap> orderList = DbUp.upTable("oc_orderinfo").queryAll(
					"order_code,order_status,buyerCode", "", "",
					new MDataMap("big_order_code", orderCode,"seller_code",sellerCode));
			if( null != orderList && orderList.size() > 0) {
				for (MDataMap orderInfo : orderList) {
					if(getUserCode().equals(orderInfo.get("buyerCode"))) {
						if(failedOrderStatus.equals(orderInfo.get("order_status")) || successedOrderStatus.equals(orderInfo.get("order_status"))) {
							deleteOrder.add(orderInfo.get("order_code"));
						} else {
							errorOrder.add(orderInfo.get("order_code"));
						}
					} else {
						result.setResultCode(0);
						result.setResultMessage("删除订单失败,不是本人的订单");
						return result;
					}
					
				}
			}
			
		} else {//DD小单
			OrderService service = new OrderService();
			Order order = service.getOrder(inputParam.getOrderCode());
			if(order!=null){
				String buyerCode = order.getBuyerCode();
				if(buyerCode.equals(getUserCode())){
					if(failedOrderStatus.equals(order.getOrderStatus()) || successedOrderStatus.equals(order.getOrderStatus())) {
						deleteOrder.add(order.getOrderCode());
					} else {
						errorOrder.add(order.getOrderCode());
					}
				}else{
					result.setResultCode(0);
					result.setResultMessage("删除订单失败,不是本人的订单");
					return result;
				}
			}
		}
		
		if(deleteOrder.size() > 0) {
			result = deleteOrder(deleteOrder ,sellerCode);//删除交易成功的订单
		} 
		
		if(errorOrder.size() > 0) {
			result.setResultCode(0);
			result.setResultMessage("失败订单："+JSON.toJSONString(errorOrder));
		}
		return result;
	}
	
	/**
	 * 删除订单
	 * 只更改删除状态
	 * @param curntOrderStatus
	 * @param orderCode
	 * @return
	 */
	public RootResultWeb deleteOrder (List<String> orderCodeList,String seller_code) {
		RootResultWeb result = new RootResultWeb();
		String orderCodes = StringUtils.join(orderCodeList, "','");
		orderCodes = "'" + orderCodes + "'";
		
		String execSql = "update oc_orderinfo set delete_flag = 1 where order_code in("
				+ orderCodes
				+ ") and seller_code =:sellerCode and (order_status = :failedOrderStatus or order_status = :successedOrderStatus)";
		
		try {
			DbUp.upTable("oc_orderinfo").dataExec(execSql, new MDataMap("sellerCode",getManageCode(),"failedOrderStatus",failedOrderStatus,"successedOrderStatus",successedOrderStatus));
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("删除订单失败");
			return result;
		}
		return result;
	}
	
	
	
	/*public RootResultWeb Process(ApiForOrderDeleteInput inputParam,MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		OrderService service = new OrderService();
		String sellerCode = getManageCode();
		if(inputParam.getOrderCode().startsWith("OS")){//以OS开头的是大订单编号
			List<MDataMap> orderList = DbUp.upTable("oc_orderinfo").queryAll(
					"order_code,seller_code", "", "",
					new MDataMap("big_order_code", inputParam.getOrderCode(),"seller_code",sellerCode));
			if(orderList != null && orderList.size() > 0){
				List<String> orderCodeList = new ArrayList<String>();
				for (int i = 0; i < orderList.size(); i++) {
					String orderCode = orderList.get(i).get("order_code");
					if(i == 0){
						Order order = service.getOrder(orderCode);
						if(order!=null){
							String buyerCode = order.getBuyerCode();
							if(!buyerCode.equals(getUserCode())){
								result.setResultCode(0);
								result.setResultMessage("删除订单失败,不是本人的订单");
							}
						}
					}
					orderCodeList.add(orderCode);
				}
				result = this.deleteOrder(StringUtils.join(orderCodeList, "','"));
				
			}
		}else {
			Order order = service.getOrder(inputParam.getOrderCode());
			if(order!=null){
				String buyerCode = order.getBuyerCode();
				if(!buyerCode.equals(getUserCode())){
					result.setResultCode(0);
					result.setResultMessage("删除订单失败,不是本人的订单");
				}else{
					result = this.deleteOrder(order.getOrderCode());
				}
			}
		}
		return result;
	}
	
	*//** 
	* @Description: 根据订单编号删除订单（逻辑删除）
	* @param orderString 一个或多个订单编号字符串
	* @author 张海生
	* @date 2015-8-13 上午10:26:09
	* @return RootResultWeb 
	* @throws 
	*//*
	public RootResultWeb deleteOrder(String orderString){
		RootResultWeb result = new RootResultWeb();
		String upSql = "update oc_orderinfo set delete_flag = 1 where order_code in('"
				+ orderString
				+ "') and seller_code =:sellerCode and order_status=:orderStatus";
		try {
			DbUp.upTable("oc_orderinfo").dataExec(upSql, new MDataMap("sellerCode",getManageCode(),"orderStatus",orderStatus));
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("删除订单失败,不是本人的订单");
			return result;
		}
		return result;
	}*/
}
