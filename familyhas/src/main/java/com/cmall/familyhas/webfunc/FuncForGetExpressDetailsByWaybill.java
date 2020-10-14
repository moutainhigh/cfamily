package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncForGetExpressDetailsByWaybill extends RootFunc{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap shipmentsInfo = DbUp.upTable("oc_order_shipments").one("order_code",mDataMap.get("flowbussinessid"));
		MWebResult result = new MWebResult();
		if(shipmentsInfo == null || shipmentsInfo.isEmpty()) {
			result.setResultCode(-1);
			result.setResultMessage("暂无物流消息");
			return result;
		}
		String orderCode = shipmentsInfo.get("order_code");
		String logisticse_code = shipmentsInfo.get("logisticse_code");
		String waybill = shipmentsInfo.get("waybill");
		if(StringUtils.isEmpty(logisticse_code)) {
			result.setResultCode(-1);
			result.setResultMessage("暂无物流消息");
			return result;
		}
		if(StringUtils.isEmpty(orderCode)) {
			result.setResultCode(-1);
			result.setResultMessage("暂无物流消息");
			return result;
		}
		if(StringUtils.isEmpty(waybill)) {
			result.setResultCode(-1);
			result.setResultMessage("暂无物流消息");
			return result;
		}
		String sql = "SELECT * FROM ordercenter.oc_express_detail where order_code = '"+orderCode+"' AND logisticse_code = '"+logisticse_code+"' AND waybill = '"+waybill+"'";
		List<Map<String,Object>> list = DbUp.upTable("oc_express_detail").dataSqlList(sql, null);
		if(list == null || list.size() == 0) {
			result.setResultCode(-1);
			result.setResultMessage("暂无物流信息");
			return result;
		}
		for(Map<String,Object> map : list) {
			ExpressDetailsResult express =  new ExpressDetailsResult();
			express.setAreaCode(StringUtils.trimToEmpty(map.get("areaCode").toString()));
			express.setAreaName(StringUtils.trimToEmpty(map.get("areaName").toString()));
			express.setContext(StringUtils.trimToEmpty(map.get("context").toString()));
			express.setLogisticse_code(StringUtils.trimToEmpty(map.get("logisticse_code").toString()));
			express.setOrder_code(StringUtils.trimToEmpty(map.get("order_code").toString()));
			express.setStatus(StringUtils.trimToEmpty(map.get("status").toString()));
			express.setTime(StringUtils.trimToEmpty(map.get("time").toString()));
			express.setWaybill(StringUtils.trimToEmpty(map.get("waybill").toString()));
			result.getResultList().add(express);
		}
		return result;
	}

}
