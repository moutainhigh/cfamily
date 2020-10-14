package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 5.2.8 专题增加活动商品按开始时间筛选
 * @author cc
 *
 */
public class CommodityService extends BaseClass {

	/**
	 * 查询专题开始时间
	 * @param template_number
	 * @return
	 */
	public List<String> getDistinctTime(String template_number){
		List<String> ret = new ArrayList<String>();
		String sql = "select distinct start_time from familyhas.fh_data_commodity where template_number=:template_number and dal_status=:dal_status order by start_time desc";
		List<Map<String,Object>> list = DbUp.upTable("fh_data_commodity").dataSqlList(sql, new MDataMap("template_number", template_number, "dal_status", "1001"));
		if(list == null || list.size() <= 0) {
			return new ArrayList<String>();
		}
		for(Map<String,Object> map : list) {
			String start_time = map.get("start_time") == null ? "" : map.get("start_time").toString();
			start_time = start_time.trim();
			if(!"".equals(start_time)) {
				int index = start_time.indexOf(" ");
				if(index > 0) {
					start_time = start_time.substring(0, index);					
				}	
				if(!ret.contains(start_time)) {
					ret.add(start_time);
				}
			}		
		}
		return ret;
	}
}
