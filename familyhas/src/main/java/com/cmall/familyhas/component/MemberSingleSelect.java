package com.cmall.familyhas.component;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webcomponent.ComponentWindowSingle;
import com.srnpr.zapweb.webmodel.MWebField;

/**
 * 根据手机号选择用户
 * @author jlin
 *
 */
public class MemberSingleSelect extends ComponentWindowSingle {

	@Override
	public String upText(MWebField mWebField, MDataMap mDataMap, int iType) {
		
		mWebField.setSourceParam("zw_s_max_select=1&zw_s_source_tableinfo=mc_login_info|member_code|login_name&zw_s_source_page=page_chart_v_mc_login_info_cf");
		
		return super.upText(mWebField, mDataMap, iType);
	}
	
}
