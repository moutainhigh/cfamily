package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetNewDlqByTvInput;
import com.cmall.familyhas.api.result.ApiGetNewDlqByTvResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetNewDlqByTv extends RootApiForManage<ApiGetNewDlqByTvResult,ApiGetNewDlqByTvInput>{

	public ApiGetNewDlqByTvResult Process(ApiGetNewDlqByTvInput inputParam,
			MDataMap mRequestMap) {
		
		ApiGetNewDlqByTvResult result = new ApiGetNewDlqByTvResult();
		
		
		MDataMap paramMap = new MDataMap();
		paramMap.put("page_status", "1");
		paramMap.put("tv_number", inputParam.getTvNumber());
//		paramMap.put("delete_state", "1001");
		
		
//		String sSql = "SELECT page_number FROM familyhas.fh_dlq_page WHERE state = :state AND page_number IN (" +
//				"SELECT page_number FROM familyhas.fh_dlq_content WHERE tv_number= :tv_number and delete_state = :delete_state  GROUP BY page_number" +
//				") ORDER BY zid DESC LIMIT 0,1 ";
		String sSql = " SELECT page_number FROM familyhas.fh_dlq_page WHERE page_number IN ( " +
					" SELECT page_number FROM familyhas.fh_dlq_status WHERE page_status = :page_status AND tv_number = :tv_number " +
				" ) " +
				" ORDER BY  cast(page_sort as signed) DESC  LIMIT 0,1 ";
		
		
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_dlq_page").dataSqlList(sSql, paramMap);
		if(null != dataSqlList && dataSqlList.size() > 0) {
			Map<String, Object> map = dataSqlList.get(0);
			result.setPageNum(String.valueOf(map.get("page_number")));
		} else {
			result.setResultCode(0);
			result.setResultMessage("没有该电视节目的最新信息");
		}
		
		
		return result;
	}

}
