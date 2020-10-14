package com.cmall.familyhas.component;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webcomponent.ComponentWindowSingle;
import com.srnpr.zapweb.webmodel.MWebField;

public class OrderSingleSelect extends ComponentWindowSingle {

	@Override
	public String upText(MWebField mWebField, MDataMap mDataMap, int iType) {
		
		mWebField.setSourceParam("zw_s_max_select=1&zw_s_source_tableinfo=oc_orderinfo|order_code|seller_code|product_name&zw_s_source_page=page_chart_v_oc_orderinfo_cf");
		
		return super.upText(mWebField, mDataMap, iType);
	}
	
}
