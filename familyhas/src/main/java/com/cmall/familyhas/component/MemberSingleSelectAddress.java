package com.cmall.familyhas.component;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webcomponent.ComponentWindowSingle;
import com.srnpr.zapweb.webmodel.MWebField;

public class MemberSingleSelectAddress extends ComponentWindowSingle {
	
	@Override
	public String upText(MWebField mWebField, MDataMap mDataMap, int iType) {

		mWebField.setSourceParam("zw_s_max_select=1&zw_s_source_tableinfo=v_base_uc_sellerinfo_adderss_choose|small_seller_code|seller_company_name|create_time|end_time|legal_person|registration_number&zw_s_source_page=page_chart_v_base_uc_sellerinfo_adderss_choose");
		//mWebField.setSourceParam("zw_s_max_select=1&zw_s_source_tableinfo=v_sellororder|buyer_code|mobilephone|order_money|order_status|receiver_person|product_name|pay_type|big_order_code&zw_s_source_page=page_chart_v_sellororder_return_goods_choose");

		return super.upText(mWebField, mDataMap, iType);
	}
}
