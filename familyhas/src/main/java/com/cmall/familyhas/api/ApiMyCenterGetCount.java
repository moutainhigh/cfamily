package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiMyCenterConfigInput;
import com.cmall.familyhas.api.result.ApiMyCenterConfigResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiMyCenterGetCount extends RootApiForManage<ApiMyCenterConfigResult, ApiMyCenterConfigInput> {

	@Override
	public ApiMyCenterConfigResult Process(ApiMyCenterConfigInput inputParam, MDataMap mRequestMap) {
		boolean flag = true;
		String msg = "";
		int count = 0;
		String isAdd = mRequestMap.get("isAdd");
		if("Y".equals(isAdd)) {
			count = DbUp.upTable("fh_center_config").dataCount("", new MDataMap());
			if(count >= 12) {
				flag = false;
				msg = "个人中心配置最多12条数据，请删除再添加。";
			}
		}
		
		String position = mRequestMap.get("position");
		List<Map<String, Object>> list = DbUp.upTable("fh_center_config").upTemplate().queryForList("select * from fh_center_config where position = '" + position + "'", new MDataMap());
		if("Y".equals(isAdd) && list.size() > 0) {
			flag = false;
			msg = "此位置的记录已存在";
		}else if("N".equals(isAdd) && list.size() > 0) {
			String zw_f_uid = mRequestMap.get("zw_f_uid");
			Map<String, Object> map = list.get(0);
			if(!zw_f_uid.equals(MapUtils.getString(map, "uid"))) {
				flag = false;
				msg = "此位置的记录已存在";
			}
		}
		
		ApiMyCenterConfigResult result = new ApiMyCenterConfigResult();
		result.setSuccess(flag);
		result.setMsg(msg);
		return result;
	}
}
