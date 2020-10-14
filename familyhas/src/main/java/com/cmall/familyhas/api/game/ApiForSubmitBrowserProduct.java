package com.cmall.familyhas.api.game;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.game.input.ApiForSubmitBrowserProductInput;
import com.cmall.familyhas.api.game.result.ApiForSubmitBrowserProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户提交浏览商品任务接口
 * 
 * @author Angel Joy
 * @date 20190925
 */
public class ApiForSubmitBrowserProduct extends RootApiForToken<ApiForSubmitBrowserProductResult, ApiForSubmitBrowserProductInput> {

	@Override
	public ApiForSubmitBrowserProductResult Process(ApiForSubmitBrowserProductInput inputParam, MDataMap mRequestMap) {
		ApiForSubmitBrowserProductResult result = new ApiForSubmitBrowserProductResult();
		String userCode = getUserCode();
		String gameCode = inputParam.getGameCode();
		MDataMap browserInsertMap = new MDataMap();
		browserInsertMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		browserInsertMap.put("game_code", gameCode);
		browserInsertMap.put("user_code", userCode);
		browserInsertMap.put("create_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("sc_hudong_browse_product").dataInsert(browserInsertMap);
		/**
		 * 计算剩余次数。
		 */
		MDataMap taskinfo = DbUp.upTable("sc_hudong_game_taskinfo").one("game_code",gameCode,"task_type","4497471600530004");
		String today = DateUtil.getNoSpSysDateString2();
		if(taskinfo == null || taskinfo.isEmpty()) {
			result.setResidualTimes(0);
		}else {
			Integer repeatTimes = Integer.parseInt(StringUtils.isEmpty(taskinfo.get("repeat_times"))?"0":taskinfo.get("repeat_times"));
			String sql = "SELECT * FROM systemcenter.sc_hudong_browse_product WHERE game_code = '"+gameCode+"' AND user_code = '"+userCode+"' AND create_time > '"+today+" 00:00:00'";
			List<Map<String,Object>> list = DbUp.upTable("sc_hudong_browse_product").dataSqlList(sql, null);
			if(list == null || list.size() == 0) {
				result.setResidualTimes(repeatTimes -1);
			}else if(repeatTimes - list.size() <= 0){
				result.setResidualTimes(0);
			}else {
				result.setResidualTimes(repeatTimes - list.size());
			}
		}
		return result;
		
	}

}
