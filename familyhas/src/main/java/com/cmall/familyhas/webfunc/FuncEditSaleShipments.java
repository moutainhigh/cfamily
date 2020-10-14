package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.ordercenter.express.service.OrderShipmentsService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改售后物流
 * @author jlin
 *
 */
public class FuncEditSaleShipments extends FuncEdit {
	
	static OrderShipmentsService shipmentsService = new OrderShipmentsService();
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		
		String uid=mDataMap.get("zw_f_uid");
		String logisticse_code=mDataMap.get("zw_f_logisticse_code");
		String waybill=mDataMap.get("zw_f_waybill");
		
		MDataMap shipmentsInfo=DbUp.upTable("oc_order_shipments").one("uid",uid);
		DbUp.upTable("oc_order_shipments").dataUpdate(new MDataMap("uid",uid,"order_code",shipmentsInfo.get("order_code")+".bak","shipments_code",shipmentsInfo.get("order_code")+".bak"), "", "uid");
		DbUp.upTable("oc_order_shipments_ext").dataUpdate(new MDataMap("order_code",shipmentsInfo.get("order_code")+".bak","shipments_code",shipmentsInfo.get("order_code")),"order_code" , "shipments_code");
		 
		
		String now=DateUtil.getSysDateTimeString();
		MDataMap addMap=new MDataMap();
		addMap.put("order_code",shipmentsInfo.get("order_code"));
		addMap.put("logisticse_code", logisticse_code);
		addMap.put("waybill", waybill);
		addMap.put("freight_money", mDataMap.get("zw_f_freight_money"));
		addMap.put("logisticse_name", (String)DbUp.upTable("sc_logisticscompany").dataGet("company_name", "company_code=:company_code", new MDataMap("company_code",logisticse_code)));
		addMap.put("creator", UserFactory.INSTANCE.create().getUserCode());
		addMap.put("create_time", now);
		addMap.put("update_time", now);
		addMap.put("update_user", addMap.get("creator"));
		addMap.put("shipments_code", shipmentsInfo.get("shipments_code"));
		
		
		DbUp.upTable("oc_order_shipments").dataInsert(addMap);
		
		if(!shipmentsInfo.get("logisticse_code").equals(logisticse_code) || !shipmentsInfo.get("waybill").equals(waybill)) {
			shipmentsService.onChangeShipment(shipmentsInfo.get("order_code"), shipmentsInfo.get("logisticse_code"), shipmentsInfo.get("waybill"), logisticse_code, waybill);
		}
		
		return mResult;
	}
}
