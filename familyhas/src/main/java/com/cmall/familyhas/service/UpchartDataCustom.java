package com.cmall.familyhas.service;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webpage.ControlPage;
import com.srnpr.zapweb.webpage.RootExec;

public class UpchartDataCustom {
	public MPageData upChartData(ControlPage page) {
		MWebPage webPage = page.getWebPage();
		MDataMap reqMap = page.getReqMap();
		if(reqMap.get("zw_f_category_code") != null) {
			String category_code = reqMap.get("zw_f_category_code").toString();
			page.getReqMap().put("sub_query", "product_code IN (SELECT ucr.product_code AS product_code FROM usercenter.uc_sellercategory_product_relation ucr WHERE ucr.category_code like '"+category_code+"%')");
		}
		
		return new RootExec().upChartData(webPage, page.getReqMap(), new MDataMap());
	}
}
