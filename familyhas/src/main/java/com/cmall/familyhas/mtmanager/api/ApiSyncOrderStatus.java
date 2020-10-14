package com.cmall.familyhas.mtmanager.api;

import java.util.Map;

import com.cmall.familyhas.mtmanager.inputresult.SyncOrderStatusInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncOrderStatusResult;
import com.cmall.familyhas.mtmanager.model.OrderStatusModel;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiSyncOrderStatus extends
		RootApiForManage<SyncOrderStatusResult, SyncOrderStatusInput> {
	public SyncOrderStatusResult Process(SyncOrderStatusInput inputParam,MDataMap mRequestMap) {
		SyncOrderStatusResult srs=new SyncOrderStatusResult();
		OrderStatusModel osm=new OrderStatusModel();
		String order_status="";
		String logisticse_name="";
		String waybill="";
		String create_time="";
		if (inputParam != null && !inputParam.equals("")) {
			String[] ordercodes = inputParam.getOrderCodes().split(",");
			String order_code;
			for (int i = 0; i < ordercodes.length; i++) {
				order_code = ordercodes[i];
				Map<String, String> orderstatusmap = DbUp.upTable("oc_orderinfo").oneWhere("order_status,order_code", "","order_code=:order_code", "order_code",	order_code);
					if(orderstatusmap!=null){
						order_status = orderstatusmap.get("order_status")==null?"":orderstatusmap.get("order_status").toString();
					}
				Map<String, String> shipments = DbUp.upTable("oc_order_shipments").oneWhere("logisticse_name,waybill,order_code", "","order_code=:order_code", "order_code", order_code);
				if (shipments != null) {
					logisticse_name = shipments.get("logisticse_name") == null ? ""	: shipments.get("logisticse_name").toString();
					waybill = shipments.get("waybill") == null ? "" : shipments	.get("waybill").toString();
				}
				
				MDataMap mDataMap = DbUp.upTable("lc_orderstatus").oneWhere("create_time", "", "code=:order_code AND now_status=:order_status","order_code",order_code, "order_status","4497153900010003");
				if(null!=mDataMap){
					create_time=mDataMap.get("create_time")==null?"":mDataMap.get("create_time").toString();
				}
					osm.setSendTime(create_time);
					osm.setOrderCode(order_code);
					osm.setOrderStstus(order_status);
					osm.setLogisticseName(logisticse_name);
					osm.setWayBIll(waybill);
					srs.getListInfo().add(osm);
			}
		} else {
			srs.setResultCode(916421176);
			srs.setResultMessage(bInfo(916421176));
			return srs;
		}
		return srs;
	}

}
