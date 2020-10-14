package com.cmall.familyhas.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiSearchWordsInput;
import com.cmall.familyhas.api.model.SearchWords;
import com.cmall.familyhas.api.result.ApiSearchWordsResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiSearchWords extends RootApiForVersion<ApiSearchWordsResult, ApiSearchWordsInput> {

	@Override
	public ApiSearchWordsResult Process(ApiSearchWordsInput inputParam, MDataMap mRequestMap) {
		ApiSearchWordsResult result = new ApiSearchWordsResult();
		// 是否显示推广收益 4497471600610001: 推广收益 4497473700010001：开启状态
		try {
			result.setPromote_is_show(DbUp.upTable("fh_tgz_profit_setting").count("tgz_type", "4497471600610001",
					"status", "4497473700010001"));
		} catch (Exception e) {
			e.printStackTrace();

		}
		// 因小程序审核不通过，暂时先屏蔽
		if ("449747430023".equals(getChannelId())) {
			return result;
		}

		String formatcurdate = DateUtil.sdfDateTime.format(new Date());
		String sql = "SELECT\r\n" + "	*\r\n" + "FROM\r\n" + "	fh_search_words\r\n" + "WHERE\r\n"
				+ "	'%s' >= begin_time\r\n" + "AND '%s' <= end_time";
		sql = String.format(sql, formatcurdate, formatcurdate);
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_search_words").dataSqlList(sql, new MDataMap());
		List<SearchWords> parseArray = JSONObject.parseArray(JSONObject.toJSONString(dataSqlList), SearchWords.class);
		result.setList(parseArray);

		return result;
	}

}
