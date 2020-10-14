package com.cmall.familyhas.pagehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 取商户名称<br>
 * 传入商户编号多个用逗号分隔：code1,code2,code3
 */
public class SmallSellerNameHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		if(params == null || params.length == 0) {
			return "";
		}
		
		List<String> nameList = new ArrayList<String>();
		
		String[] vs = params[0].toString().split(",");
		List<String> codes = new ArrayList<String>();
		for(String v : vs) {
			if(StringUtils.isNotBlank(v)) {
				if("SI2003".equalsIgnoreCase(v)) {
					nameList.add("LD自营");
				} else {
					codes.add("'"+StringUtils.trimToEmpty(v)+"'");
				}
			}
		}
		
		if(!codes.isEmpty()) {
			List<MDataMap> mapList = DbUp.upTable("uc_sellerinfo").queryAll("seller_company_name", "", "small_seller_code IN("+StringUtils.join(codes,",")+")", new MDataMap());
			for(MDataMap m : mapList) {
				nameList.add(m.get("seller_company_name"));
			}
		}
		
		return StringUtils.join(nameList,",");
	}

}
