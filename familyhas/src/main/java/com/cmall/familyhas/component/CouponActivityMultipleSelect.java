package com.cmall.familyhas.component;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webcomponent.ComponentWindowSingle;
import com.srnpr.zapweb.webmodel.MWebField;

/**
 * 优惠券活动多选控件（惠家有）
 * @author wei.che
 * @Date 2015-12-17 
 */
public class CouponActivityMultipleSelect extends ComponentWindowSingle {
	
	@Override
	public String upText(MWebField mWebField, MDataMap mDataMap, int iType) {
		
		
		mWebField.setSourceParam("zw_s_max_select=100&zw_s_source_tableinfo=oc_activity|activity_code|activity_name&zw_s_source_page=page_chart_v_multi_select_coupon_oc_activity_new3&zw_f_seller_code=SI2003");
		
		
		
		return upShowText(mWebField, mDataMap, iType);
	}
	
}
