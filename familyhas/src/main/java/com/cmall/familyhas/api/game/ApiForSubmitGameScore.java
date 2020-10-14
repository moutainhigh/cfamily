package com.cmall.familyhas.api.game;

import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.game.input.ApiForSubmitGameScoreInput;
import com.cmall.familyhas.api.game.result.ApiForSubmitGameScoreResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
/**
 * 用户提交所得分数接口
 * 提交一次，用户可玩次数在当前基础上减一
 * sc_hudong_game_score 表新增一条数据
 * @author Angel Joy
 *
 */
public class ApiForSubmitGameScore extends RootApiForToken<ApiForSubmitGameScoreResult,ApiForSubmitGameScoreInput>{

	@Override
	public ApiForSubmitGameScoreResult Process(ApiForSubmitGameScoreInput inputParam, MDataMap mRequestMap) {
		ApiForSubmitGameScoreResult  result = new ApiForSubmitGameScoreResult();
		String gameCode = inputParam.getGameCode();
		Integer score = inputParam.getScore();
		String userCode = getUserCode();
		Integer type = inputParam.getType();
		if(type == 1) {//减次数
			result = this.timesHandle(gameCode, userCode);
		}else if(type == 2){//提交分数
			result = this.submitScore(gameCode, userCode, score);
		}else {
			result.setResultCode(999999);
			result.setResultMessage("类型不合法");
			return result;
		}
		return result;
	}
	
	private ApiForSubmitGameScoreResult submitScore(String gameCode,String userCode,Integer score) {
		ApiForSubmitGameScoreResult result = new ApiForSubmitGameScoreResult();
		MDataMap gameScoreMap = new MDataMap();
		MDataMap gameInfo = DbUp.upTable("sc_hudong_game").one("game_code",gameCode);
		if(gameInfo == null || gameInfo.isEmpty()) {
			result.setResultCode(999999);
			result.setResultMessage("游戏不存在！");
			return result;
		}
		gameScoreMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		gameScoreMap.put("game_code", gameCode);
		gameScoreMap.put("user_code", userCode);
		gameScoreMap.put("game_name", gameInfo.get("game_name"));
		gameScoreMap.put("score", score.toString());
		gameScoreMap.put("score_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("sc_hudong_game_score").dataInsert(gameScoreMap);
		//获取最高分
		String sql = "SELECT MAX(score) maxscore FROM systemcenter.sc_hudong_game_score WHERE game_code = '"+gameCode+"' AND user_code = '"+userCode+"' AND score_time >= '"+DateUtil.getNoSpSysDateString2()+" 00:00:00'";
		Map<String,Object> scoreMap = DbUp.upTable("sc_hudong_game_score").dataSqlOne(sql, null);
		if(scoreMap == null || scoreMap.get("maxscore") == null) {
			result.setMaxScore(score);
		}else{
			Integer maxScore = Integer.parseInt(scoreMap.get("maxscore").toString());
			if(maxScore >= score) {
				result.setMaxScore(maxScore);
			}else {
				result.setMaxScore(score);
			}
		}
		MDataMap userTimes = DbUp.upTable("sc_hudong_game_user").one("user_code",userCode,"game_code",gameCode);
		if(userTimes == null) {
			result.setGame_times(Integer.parseInt(bConfig("familyhas.game_times"))-1);
		}else {
			result.setGame_times(Integer.parseInt(userTimes.get("game_times")));
		}
		return result;
	}
	
	/**
	 * 减次数操作
	 * @param gameCode
	 * @param userCode
	 * @return
	 */
	private  ApiForSubmitGameScoreResult timesHandle(String gameCode,String userCode) {
		ApiForSubmitGameScoreResult result = new ApiForSubmitGameScoreResult();
		MDataMap gameInfo = DbUp.upTable("sc_hudong_game").one("game_code",gameCode);
		if(gameInfo == null || gameInfo.isEmpty()) {
			result.setResultCode(999999);
			result.setResultMessage("游戏不存在！");
			return result;
		}
		//获取游戏次数
		MDataMap userTimesMap = DbUp.upTable("sc_hudong_game_user").one("user_code",userCode,"game_code",gameCode);
		String playTimes = bConfig("familyhas.game_times");
		Integer times = Integer.parseInt(playTimes) -1;
		if(userTimesMap  == null || userTimesMap.isEmpty()) {//用户首次提交
			MDataMap insert = new MDataMap();
			insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
			insert.put("game_code", gameCode);
			insert.put("user_code", userCode);
			insert.put("game_name", gameInfo.get("game_name"));
			insert.put("game_times",times.toString());
			insert.put("date_time",DateUtil.getNoSpSysDateString2());
			DbUp.upTable("sc_hudong_game_user").dataInsert(insert);
		}else {
			String date_time = userTimesMap.get("date_time");//获取时间
			if(date_time.equals(DateUtil.getNoSpSysDateString2())) {//今天的次数
				times = Integer.parseInt(userTimesMap.get("game_times")) - 1;
				if(times<0) {
					result.setResultCode(999999);
					result.setResultMessage("次数已经用完");
					return result;
				}
				userTimesMap.put("game_times", times.toString());
				DbUp.upTable("sc_hudong_game_user").dataUpdate(userTimesMap,"game_times","uid");
			}else {
				userTimesMap.put("game_times", times.toString());
				userTimesMap.put("date_time", DateUtil.getNoSpSysDateString2());
				DbUp.upTable("sc_hudong_game_user").dataUpdate(userTimesMap,"game_times,date_time","uid");
			}
		}
		result.setGame_times(times);
		return result;
	}

	
}
