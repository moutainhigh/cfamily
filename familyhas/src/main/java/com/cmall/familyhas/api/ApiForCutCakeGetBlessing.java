package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForCutCakeGetBlessingInput;
import com.cmall.familyhas.api.result.ApiForCutCakeGetBlessingResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取切蛋糕祝福语(随机取200条)
 * @author lgx
 *
 */
public class ApiForCutCakeGetBlessing extends RootApiForVersion<ApiForCutCakeGetBlessingResult, ApiForCutCakeGetBlessingInput> {

	public ApiForCutCakeGetBlessingResult Process(ApiForCutCakeGetBlessingInput inputParam, MDataMap mRequestMap) {
		
		ApiForCutCakeGetBlessingResult result = new ApiForCutCakeGetBlessingResult();
		
		List<String> list = new ArrayList<String>();
		
		// 随机查询200条切蛋糕祝福语
		String sql = "SELECT * FROM sc_hudong_cake_blessing WHERE check_status = '449748580001' order by rand() limit 200";
		List<Map<String, Object>> cakeBlessList = DbUp.upTable("sc_hudong_cake_blessing").dataSqlList(sql, new MDataMap());
		if(cakeBlessList != null && cakeBlessList.size() > 0) {
			for (Map<String, Object> map : cakeBlessList) {
				String cake_blessing = MapUtils.getString(map, "cake_blessing");
				list.add(cake_blessing);
			}
		}
		
		result.setList(list);
		
		return result;
	}


}
