package com.cmall.familyhas.webfunc;


import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 属性值上移/下移
 */
public class FuncPropertiesValueUpDown extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String properties_value_code = mDataMap.get("zw_f_properties_value_code");
		String type = mDataMap.get("zw_f_type");
		
		// 验证是否能够移动
		MDataMap pValue = DbUp.upTable("uc_properties_value").one("properties_value_code",properties_value_code,"is_delete","0");
		if(null == pValue) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("属性值不存在");
			return mResult;
		}
		String properties_code = pValue.get("properties_code");
		String sql = "SELECT * FROM uc_properties_value WHERE properties_code = '"+properties_code+"' AND is_delete = '0' ORDER BY sort_num ASC";
		List<Map<String, Object>> list = DbUp.upTable("uc_properties_value").dataSqlList(sql, new MDataMap());
		if(null != list && list.size() > 0) {			
			if(list.size() > 1) {
				// 选中移动属性值所处的位置
				int num = 0;
				for (Map<String, Object> map : list) {
					num++;
					if(properties_value_code.equals(map.get("properties_value_code"))) {
						break;
					}
				}
				Map<String, Object> map1 = list.get(num-1);
				int sort_num1 = (int) map1.get("sort_num");
				
				Map<String, Object> map2 = null;
				if("UP".equals(type)) {
					map2 = list.get(num-2);
				}else if("DOWN".equals(type)) {
					map2 = list.get(num);
				}else {
					mResult.setResultCode(-1);
					mResult.setResultMessage("移动类型有误");
					return mResult;
				}
				if(map2 == null) {
					mResult.setResultCode(-1);
					mResult.setResultMessage("交换对象有误");
					return mResult;
				}
				String properties_value_code2 = (String) map2.get("properties_value_code");
				int sort_num2 = (int) map2.get("sort_num");
				
				MDataMap mDataMap1 = new MDataMap();
				mDataMap1.put("properties_value_code", properties_value_code);
				mDataMap1.put("sort_num", sort_num2+"");
				DbUp.upTable("uc_properties_value").dataUpdate(mDataMap1, "sort_num", "properties_value_code");
				
				MDataMap mDataMap2 = new MDataMap();
				mDataMap2.put("properties_value_code", properties_value_code2);
				mDataMap2.put("sort_num", sort_num1+"");
				DbUp.upTable("uc_properties_value").dataUpdate(mDataMap2, "sort_num", "properties_value_code");
			}else {
				mResult.setResultCode(-1);
				mResult.setResultMessage("只有一个属性值,不需要移动");
				return mResult;
			}
		}else {
			mResult.setResultCode(-1);
			mResult.setResultMessage("没有可操作属性值");
			return mResult;
		}
		
		return mResult;
	}

}
