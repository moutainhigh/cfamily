package com.cmall.familyhas.pagehelper;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 取公众号渠道有效订单量<br>
 */
public class SmallAppChannelOrderCountHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		if(params == null || params.length == 0) {
			return "";
		}
		
		String sql = "SELECT COUNT(DISTINCT o.order_code) num FROM fh_smallapp_order s, ordercenter.oc_orderinfo o "
				+ " WHERE s.big_order_code = o.big_order_code AND s.ou_channel_id = :channel_id AND o.order_status not in('4497153900010001','4497153900010006')";
		
		Map<String, Object> map = DbUp.upTable("fh_smallapp_order").dataSqlOne(sql, new MDataMap("channel_id", params[0].toString()));
		
		return NumberUtils.toInt(map.get("num").toString());
	}

}
