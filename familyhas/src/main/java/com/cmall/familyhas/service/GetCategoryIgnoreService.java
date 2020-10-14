package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/** 
* @author Angel Joy
* @Time 2020年7月3日 上午11:36:25 
* @Version 1.0
* <p>Description:</p>
*/
public class GetCategoryIgnoreService extends BaseClass {
	
	public String getCategory() {
		String str = "";
		List<Map<String,Object>> mapList = DbUp.upTable("uc_program_del_category").dataSqlList("SELECT * FROM usercenter.uc_program_del_category", new MDataMap());
		List<String> categoeys = new ArrayList<String>();
		for(Map<String,Object> map : mapList) {
			String categoryCode = MapUtils.getString(map, "category_code", "");
			String lvl =  MapUtils.getString(map, "level", "");
			if(StringUtils.isNotEmpty(categoryCode)) {
				MDataMap categoryInfo = DbUp.upTable("uc_sellercategory").one("category_code",categoryCode,"level",lvl);
				if(categoryInfo != null && !categoryInfo.isEmpty()) {
					categoeys.add(categoryInfo.get("category_name"));
				}
			}
		}
		str = StringUtils.join(categoeys, ",");
		return str;
	}

}
