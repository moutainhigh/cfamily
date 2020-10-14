package com.cmall.familyhas.pagehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 展示分类名称，多个编号逗号分割<br>
 * 传入参数：code1,code2,code3
 */
public class CategoryNameHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		if(params == null || params.length == 0) {
			return "";
		}
		
		String[] vs = params[0].toString().split(",");
		List<String> codes = new ArrayList<String>();
		for(String v : vs) {
			if(StringUtils.isNotBlank(v)) {
				codes.add("'"+StringUtils.trimToEmpty(v)+"'");
			}
		}
		
		List<String> nameList = new ArrayList<String>();
		if(!codes.isEmpty()) {
			List<MDataMap> mapList = DbUp.upTable("uc_sellercategory").queryAll("category_name", "", "seller_code = 'SI2003' AND category_code IN("+StringUtils.join(codes,",")+")", new MDataMap());
			for(MDataMap m : mapList) {
				nameList.add(m.get("category_name"));
			}
		}
		
		return StringUtils.join(nameList,",");
	}

}
