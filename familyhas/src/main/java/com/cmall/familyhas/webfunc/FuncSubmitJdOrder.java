package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmasorder.service.TeslaOrderServiceJD;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 三方订单处理（京东）
 * 调用京东下单接口
 */
public class FuncSubmitJdOrder extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		int province = NumberUtils.toInt(mDataMap.get("province"));
		int city = NumberUtils.toInt(mDataMap.get("city"));
		int county = NumberUtils.toInt(mDataMap.get("county"));
		int town = NumberUtils.toInt(mDataMap.get("town"));
		
		String address = mDataMap.get("address");
		String orderCode = mDataMap.get("orderCode");
		
		if(province == 0 || city == 0 || county == 0) {
			result.setResultCode(0);
			result.setResultMessage("请正确选择京东收货区域");
			return result;
		}
		
		if(StringUtils.isBlank(address)) {
			result.setResultCode(0);
			result.setResultMessage("详细收货地址不能为空");
			return result;
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		MDataMap orderAddress = DbUp.upTable("oc_orderadress").one("order_code",orderCode);
		MDataMap orderJd = DbUp.upTable("oc_order_jd").one("order_code",orderCode);
		
		// 已经取消订单的不再允许提交订单
		if("4497153900010006".equals(orderInfo.get("order_status"))) {
			result.setResultCode(0);
			result.setResultMessage("订单已经取消");
			return result;
		}
		
		if(!address.equalsIgnoreCase(orderAddress.get("address"))) {
			orderAddress.put("address", address);
			DbUp.upTable("oc_orderadress").dataUpdate(orderAddress, "address", "zid");
		}
		
		if(NumberUtils.toInt(orderJd.get("province")) != province
				|| NumberUtils.toInt(orderJd.get("city")) != city
				|| NumberUtils.toInt(orderJd.get("county")) != county
				|| NumberUtils.toInt(orderJd.get("town")) != town){
			
			orderJd.put("province", province+"");
			orderJd.put("city", city+"");
			orderJd.put("county", county+"");
			orderJd.put("town", town+"");
			DbUp.upTable("oc_order_jd").dataUpdate(orderJd, "province,city,county,town", "zid");
		}
		
		TeslaOrderServiceJD teslaOrderServiceJD = new TeslaOrderServiceJD();
		result = teslaOrderServiceJD.createOrder(orderCode);
		return result;
	}

}
