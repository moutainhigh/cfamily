package com.cmall.familyhas.service;

import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class AddressEditService extends BaseClass{
	
	public MDataMap upChartData(String uid){
		MDataMap re = new MDataMap();
		MDataMap one = DbUp.upTable("nc_address").one("uid",uid);
		if(one != null) {
			re.put("address_name", MapUtils.getString(one, "address_name"));
			re.put("address_mobile", MapUtils.getString(one, "address_mobile"));
			re.put("address_default", MapUtils.getString(one, "address_default"));
			re.put("address_street", MapUtils.getString(one, "address_street"));
			String area_code = MapUtils.getString(one, "area_code");
			MDataMap one2 = DbUp.upTable("sc_tmp").one("code",area_code);
			if(one2 != null) {
				Integer code_lvl = MapUtils.getInteger(one2, "code_lvl");
				if(code_lvl == 4) {
					re.put("four", area_code);
					
					String p_code = MapUtils.getString(one2, "p_code");
					MDataMap ad3 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("three", MapUtils.getString(ad3, "code"));
					
					p_code = MapUtils.getString(ad3, "p_code");
					MDataMap ad2 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("two", MapUtils.getString(ad2, "code"));
					
					p_code = MapUtils.getString(ad2, "p_code");
					MDataMap ad1 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("one", MapUtils.getString(ad1, "code"));
					
				}else if(code_lvl == 3) {
					
					re.put("three", area_code);
					
					String p_code = MapUtils.getString(one2, "p_code");
					MDataMap ad2 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("two", MapUtils.getString(ad2, "code"));
					
					p_code = MapUtils.getString(ad2, "p_code");
					MDataMap ad1 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("one", MapUtils.getString(ad1, "code"));
					
				}else if(code_lvl == 2) {
					
					re.put("two", area_code);
					
					String p_code = MapUtils.getString(one2, "p_code");
					MDataMap ad1 = DbUp.upTable("sc_tmp").one("code",p_code);
					re.put("one", MapUtils.getString(ad1, "code"));
					
				}else if(code_lvl == 1) {
					
					re.put("one", area_code);
					
				}
			}
		}
		
		return re;
	}
}