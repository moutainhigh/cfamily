package com.cmall.familyhas.pagehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 通用字典表名称显示，多个编号逗号分割<br>
 * 传入参数1：parent_code  父编码值
 * 传入参数2：define_code1,define_code2,define_code3  多个编码值用逗号分割
 */
public class ScDefineNameHelper implements PageHelper {

	@Override
	public Object upData(Object... params) {
		if(params == null || params.length < 2) {
			return "";
		}
		
		String[] vs = params[1].toString().split(",");
		List<String> codes = new ArrayList<String>();
		for(String v : vs) {
			if(StringUtils.isNotBlank(v)) {
				codes.add("'"+StringUtils.trimToEmpty(v)+"'");
			}
		}
		
		List<String> nameList = new ArrayList<String>();
		
		if(!codes.isEmpty()) {
			List<MDataMap> mapList = DbUp.upTable("sc_define").queryAll("define_name name", "", "parent_code = '"+params[0]+"' AND define_code IN("+StringUtils.join(codes,",")+")", new MDataMap());
			for(MDataMap m : mapList) {
				nameList.add(m.get("name"));
			}
		}
		
		return StringUtils.join(nameList,",");
	}

}
