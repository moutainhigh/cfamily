package com.cmall.familyhas.pagehelper;

import com.srnpr.zapweb.helper.PageHelper;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.webpage.ControlPage;

/**
 * 退款管理对商品渠道的自营查询做特殊处理
 */
public class ChartByProductChannel implements PageHelper{

	@Override
	public Object upData(Object... params) {
		ControlPage cp = (ControlPage)params[0];
		String productChannel = cp.getReqMap().get("zw_f_product_channel");
		
		// 自营的走一个特殊的查询条件，暂定非LD商品都算自营
		if("4497471600550004".equals(productChannel)) {
			cp.getReqMap().remove("zw_f_product_channel");
			cp.getReqMap().put("sub_query", "small_seller_code != 'SI2003'");
		}
		
		MPageData data = cp.upChartData();
		
		cp.getReqMap().put("zw_f_product_channel", productChannel);
		return data;
	}

}
