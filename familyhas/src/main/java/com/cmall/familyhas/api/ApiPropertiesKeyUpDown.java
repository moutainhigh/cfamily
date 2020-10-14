package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import com.cmall.familyhas.api.input.ApiBatchAddProductInput;
import com.cmall.familyhas.api.result.ApiBatchAddProductResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 属性上移/下移
 * @author lgx
 * 
 */
public class ApiPropertiesKeyUpDown extends RootApi<ApiBatchAddProductResult, ApiBatchAddProductInput> {

	@Override
	public ApiBatchAddProductResult Process(ApiBatchAddProductInput inputParam, MDataMap mRequestMap) {
		ApiBatchAddProductResult result = new ApiBatchAddProductResult();

		String nodeId = inputParam.getNodeId();
		String properties_code = inputParam.getProperties_code();
		String type = inputParam.getType();
		result.setNodeId(nodeId);
		
		// 验证是否能够移动
		MDataMap pKey = DbUp.upTable("uc_properties_key").one("properties_code",properties_code,"is_delete","0");
		if(null == pKey) {
			//result.setResultCode(-1);
			result.setResultMessage("属性不存在");
			return result;
		}
		MDataMap sellercategoryProperties = DbUp.upTable("uc_sellercategory_properties").one("properties_code",properties_code);
		String category_code = sellercategoryProperties.get("category_code");
		String sql = "SELECT sp.sort_num, pk.* FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
				"WHERE sp.category_code = '"+category_code+"' AND pk.is_delete = '0' ORDER BY sp.sort_num ASC";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql, new MDataMap());
		if(null != list && list.size() > 0) {			
			if(list.size() > 1) {
				// 选中移动属性所处的位置
				int num = 0;
				for (Map<String, Object> map : list) {
					num++;
					if(properties_code.equals(map.get("properties_code"))) {
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
					result.setResultCode(-1);
					result.setResultMessage("移动类型有误");
					return result;
				}
				if(map2 == null) {
					result.setResultCode(-1);
					result.setResultMessage("交换对象有误");
					return result;
				}
				String properties_code2 = (String) map2.get("properties_code");
				int sort_num2 = (int) map2.get("sort_num");
				
				MDataMap mDataMap1 = new MDataMap();
				mDataMap1.put("properties_code", properties_code);
				mDataMap1.put("sort_num", sort_num2+"");
				DbUp.upTable("uc_sellercategory_properties").dataUpdate(mDataMap1, "sort_num", "properties_code");
				
				MDataMap mDataMap2 = new MDataMap();
				mDataMap2.put("properties_code", properties_code2);
				mDataMap2.put("sort_num", sort_num1+"");
				DbUp.upTable("uc_sellercategory_properties").dataUpdate(mDataMap2, "sort_num", "properties_code");
			}else {
				//result.setResultCode(-1);
				result.setResultMessage("只有一个属性,不需要移动");
				return result;
			}
		}else {
			result.setResultCode(-1);
			result.setResultMessage("没有可操作属性");
			return result;
		}
		
		result.setResultMessage("操作成功");
		return result;
	}


}
