package com.cmall.familyhas.service;


import com.srnpr.xmasorder.period.TeslaPeriodOrder;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.baseclass.BaseClass;
/**
 * 转换输入类
 * @author shiyz
 *
 */
public class ApiConvertTeslaService extends BaseClass {
	
	
	public TeslaXResult ConvertOrder(TeslaXOrder teslaXOrder){
		
		TeslaXResult reTeslaXResult = new TeslaXResult();
		
		reTeslaXResult = new TeslaPeriodOrder().doRefresh(teslaXOrder);
		
		
		return reTeslaXResult;
		
	}

}
