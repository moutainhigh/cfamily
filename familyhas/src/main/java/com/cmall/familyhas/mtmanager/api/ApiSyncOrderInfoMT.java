package com.cmall.familyhas.mtmanager.api;

import java.math.BigDecimal;
import java.util.List;

import com.cmall.familyhas.mtmanager.model.SyncOrderInfoMTInput;
import com.cmall.familyhas.mtmanager.model.SyncOrderInfoMTResult;
import com.cmall.familyhas.mtmanager.model.SyncOrderInfoMTResult.OrderInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 同步订单信息
 * @author jlin
 *
 */
public class ApiSyncOrderInfoMT extends RootApiForManage<SyncOrderInfoMTResult, SyncOrderInfoMTInput> {

	public SyncOrderInfoMTResult Process(SyncOrderInfoMTInput inputParam, MDataMap mRequestMap) {
		
		SyncOrderInfoMTResult mtResult = new SyncOrderInfoMTResult();
		
		String start_time = inputParam.getStart_time();
		String end_time=inputParam.getEnd_time();
		
		List<MDataMap> list=DbUp.upTable("oc_orderinfo").queryAll("", "update_time", "update_time>=:start_time and update_time<=:end_time and order_status in ('4497153900010003','4497153900010004','4497153900010005') and order_type='449715200011' ", new MDataMap("end_time",end_time,"start_time",start_time));
		if(list!=null&&list.size()>0){
			
			for (MDataMap mDataMap : list) {
				
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setBuyer_code(mDataMap.get("buyer_code"));
				orderInfo.setCreate_time(mDataMap.get("create_time"));
				orderInfo.setDue_money(new BigDecimal(mDataMap.get("due_money")));
				orderInfo.setOrder_code(mDataMap.get("order_code"));
				orderInfo.setOrder_money(new BigDecimal(mDataMap.get("order_money")));
				orderInfo.setOrder_source(mDataMap.get("order_source"));
				orderInfo.setOrder_status(mDataMap.get("order_status"));
				orderInfo.setPay_type(getPayType(orderInfo.getOrder_code()));
				orderInfo.setUpdate_time(mDataMap.get("update_time"));
				
				mtResult.getOrderList().add(orderInfo);
			}
		}
		
		return mtResult;
	}
	
	private String getPayType(String order_code){
		return DbUp.upTable("oc_order_pay").oneWhere("pay_type", "zid desc", "order_code=:order_code", "order_code",order_code).get("pay_type");
	}
}
