package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class CollageStatusService extends BaseClass {

	/**
	 * 获取拼团状态
	 * @param small_seller_code
	 * @return
	 */
	public String getCollageStatus(String orderCode){
		
		if(StringUtils.isBlank(orderCode)){
			return "";
		}
		MDataMap collageItem = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(collageItem == null || collageItem.isEmpty()) {
			return "";
		}
		String collageCode = collageItem.get("collage_code");
		if(StringUtils.isBlank(collageCode)) {
			return "";
		}
		MDataMap collage = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
		if(collage == null || collage.isEmpty()) {
			return "";
		}
		String status = collage.get("collage_status");
		if("449748300001".equals(status)) {
			return "拼团中";
		}
		if("449748300002".equals(status)) {
			return "拼团成功";
		}
		if("449748300003".equals(status)) {
			return "拼团失败";
		}
		return "";
		
	}
	
	/**
	 * 
	 * @param orderCode
	 * @return
	 * 2020年4月2日
	 * Angel Joy
	 * String
	 */
	public String getCollageStatusCode(String orderCode){
		
		if(StringUtils.isBlank(orderCode)){
			return "";
		}
		MDataMap collageItem = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(collageItem == null || collageItem.isEmpty()) {
			return "";
		}
		String collageCode = collageItem.get("collage_code");
		if(StringUtils.isBlank(collageCode)) {
			return "";
		}
		MDataMap collage = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
		if(collage == null || collage.isEmpty()) {
			return "";
		}
		String status = collage.get("collage_status");
		return status;
		
	}
	
}
