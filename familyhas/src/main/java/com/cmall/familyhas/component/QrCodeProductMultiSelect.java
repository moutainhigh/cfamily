package com.cmall.familyhas.component;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webcomponent.ComponentWindowSingle;
import com.srnpr.zapweb.webmodel.MWebField;

/**
 * 商品多选控件
 * @author zgh
 *
 */
public class QrCodeProductMultiSelect extends ComponentWindowSingle {

	
	
	@Override
	public String upText(MWebField mWebField, MDataMap mDataMap, int iType) {
		
		
		mWebField.setSourceParam("zw_s_max_select=0&zw_s_source_tableinfo=pc_skuinfo|sku_code|sku_name&zw_s_source_page=page_chart_v_hjy_nc_qr_code");
		
		
		
		return upShowText(mWebField, mDataMap, iType);
	}
	
	
}
