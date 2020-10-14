package com.cmall.familyhas.api.game;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.game.input.ApiForGetGamePlayTimesInput;
import com.cmall.familyhas.api.game.result.ApiForGetGamePlayTimesResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 获取游戏可玩次数
 * @author Angel Joy
 *
 */
public class ApiForGetGamePlayTimes extends RootApiForVersion<ApiForGetGamePlayTimesResult,ApiForGetGamePlayTimesInput>{

	@Override
	public ApiForGetGamePlayTimesResult Process(ApiForGetGamePlayTimesInput inputParam, MDataMap mRequestMap) {
		ApiForGetGamePlayTimesResult result  = new ApiForGetGamePlayTimesResult();
		String gameCode = inputParam.getGameCode();
		String userCode = "";
		int times = Integer.parseInt(bConfig("familyhas.game_times"));
		if(getFlagLogin()) {
			userCode = getOauthInfo().getUserCode();
		}
		if(StringUtils.isEmpty(userCode)) {//未登录用户获取默认配置文件可玩次数
			result.setTimes(times);
		}else {
			MDataMap userTimesMap = DbUp.upTable("sc_hudong_game_user").one("game_code",gameCode,"user_code",userCode);
			if(userTimesMap == null || userTimesMap.isEmpty()) {//
				result.setTimes(times);
				//需要插入一条数据
				MDataMap gameInfo = DbUp.upTable("sc_hudong_game").one("game_code",gameCode);
				if(gameInfo == null || gameInfo.isEmpty()) {
					result.setResultCode(999999);
					result.setResultMessage("游戏不存在！！！");
					return result;
				}
				MDataMap insert = new MDataMap();
				insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
				insert.put("game_code", gameCode);
				insert.put("user_code", userCode);
				insert.put("game_name", gameInfo.get("game_name"));
				insert.put("game_times",bConfig("familyhas.game_times"));
				insert.put("date_time",DateUtil.getNoSpSysDateString2());
				DbUp.upTable("sc_hudong_game_user").dataInsert(insert);
			}else {
				String dateTime = userTimesMap.get("date_time");
				//判断时间是否是今天的数据，如果是直接取次数，如果不是，给默认次数，并且更新数据表
				String nowDate = DateUtil.getNoSpSysDateString2();
				if(dateTime.equals(nowDate)) {
					result.setTimes(Integer.parseInt(userTimesMap.get("game_times")));
				}else {
					userTimesMap.put("date_time", nowDate);
					userTimesMap.put("game_times", bConfig("familyhas.game_times"));
					DbUp.upTable("sc_hudong_game_user").dataUpdate(userTimesMap, "date_time,game_times", "uid");
					result.setTimes(times);
				}
			}
		}
		Integer allowTimes = result.getTimes();
		if(allowTimes<=0) {
			result.setTimes(0);
		}
		return result;
	}
	
}
