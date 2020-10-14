package com.cmall.familyhas.service;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取待完善订单倒计时类
 * @author Angel Joy
 *
 */
public class AfterSaleOrderForGetTime {
	
	public MDataMap getDeadLineForFillShipments(String return_code) {
		MDataMap asleInfo = DbUp.upTable("oc_order_after_sale").one("asale_code",return_code);
		String updateTime = asleInfo.get("update_time");
		String deadLine = DateUtil.addDateMinut(updateTime,24*7);
		int timeStamp = DateUtil.timeSubtraction(DateUtil.getNowTime(), deadLine, DateUtil.DATE_FORMAT_DATETIME);
		int day = timeStamp/86400;
		int hour = (timeStamp%86400)/3600;
		int min = (timeStamp%86400)%3600/60;
		MDataMap result =  new MDataMap();
		result.put("day", day+"");
		result.put("hour", hour+"");
		result.put("min", min+"");
		result.put("timeStamp", timeStamp+"");
		result.put("deadLine", deadLine);
		return  result;
	}

}
