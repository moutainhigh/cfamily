package com.cmall.familyhas.api.game;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.game.input.ApiForGameScoreListInput;
import com.cmall.familyhas.api.game.result.ApiForGameScore;
import com.cmall.familyhas.api.game.result.ApiForGameScoreListResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 游戏排行榜接口
 * 查询sc_hudong_game_score 表数据
 * @author Angel Joy
 *
 */
public class ApiForGameScoreList extends RootApiForVersion<ApiForGameScoreListResult,ApiForGameScoreListInput>{

	@Override
	public ApiForGameScoreListResult Process(ApiForGameScoreListInput inputParam, MDataMap mRequestMap) {
		ApiForGameScoreListResult result = new ApiForGameScoreListResult();
		String gameCode = inputParam.getGameCode();
		String today = DateUtil.getNoSpSysDateString2();
		String limit = inputParam.getLimit();
		ApiForGameScore myScore = new ApiForGameScore();
		if(getFlagLogin()) {
			//登陆之后查询用户自己的积分排行
			String userCode = getOauthInfo().getUserCode();
			MDataMap userInfo = this.getUserInfoByCode(userCode);
			if(userInfo == null) {
				result.setResultCode(999999);
				result.setResultMessage("登陆用户不存在");
				return result;
			}
			String sql = "SELECT MAX(score) score,user_code FROM systemcenter.sc_hudong_game_score WHERE user_code = '"+userCode+"' AND score_time >= '"+today+" 00:00:00' AND game_code = '"+gameCode+"'";
			Map<String,Object> map = DbUp.upTable("sc_hudong_game_score").dataSqlOne(sql, null);
			if(map == null || map.get("score") == null) {//未查询导数据的时候，证明用户还没有参与游戏
				myScore.setRanking("暂未上榜");
				myScore.setScore("暂无分数");
			}else {
				String score = map.get("score")!=null?map.get("score").toString():"0";
				myScore.setScore(score);
				String sqlRank ="SELECT MAX(score) max_score, user_code FROM systemcenter.sc_hudong_game_score WHERE score_time >= '"+today+" 00:00:00' AND score > "+score+" AND game_code = '"+gameCode+"' GROUP BY user_code   order by max_score desc ";//
				List<Map<String,Object>> rankings = DbUp.upTable("sc_hudong_game_score").dataSqlList(sqlRank, null);
				String rank = "1";
				if(rankings != null ) {
					rank = rankings.size()+1+"";
				}
				myScore.setRanking(rank);
			}
			myScore.setAvatar(userInfo.get("avatar"));
			myScore.setNickName(userInfo.get("nickname"));
		}
		result.setMyScore(myScore);
		String allSql = "SELECT MAX(score) max_score, user_code FROM systemcenter.sc_hudong_game_score WHERE score_time >= '"+today+" 00:00:00' AND game_code = '"+gameCode+"' GROUP BY user_code   order by max_score desc limit "+limit;
		List<Map<String,Object>> rankingList = DbUp.upTable("sc_hudong_game_score").dataSqlList(allSql, null);
		if(rankingList != null) {
			for(int i = 0;i< rankingList.size();i++) {
				MDataMap rankMap = new MDataMap(rankingList.get(i));
				String rankCode = rankMap.get("user_code");
				MDataMap userInfo = this.getUserInfoByCode(rankCode);
				ApiForGameScore rankEntity = new ApiForGameScore();
				if(userInfo == null) {
					continue;
				}
				rankEntity.setAvatar(userInfo.get("avatar"));
				rankEntity.setNickName(userInfo.get("nickname"));
				rankEntity.setScore(rankMap.get("max_score"));
				rankEntity.setRanking(i+1+"");
				result.getUserScoreList().add(rankEntity);
			}
		}
		return result;
	}
	
	private MDataMap getUserInfoByCode(String userCode) {
		MDataMap userInfo = DbUp.upTable("mc_member_sync").one("member_code",userCode);
		if(userInfo == null || userInfo.isEmpty()) {
			return null;
		}
		String nickname = userInfo.get("nickname");
		String loginname = userInfo.get("login_name");
		if(nickname.equals(loginname)&&nickname.length() == 11) {//昵称就是手机号的时候，需要处理昵称
			nickname = nickname.substring(0, 3)+"****"+nickname.substring(8,11);
		}
		userInfo.put("nickname",nickname);
		return userInfo;
	}
	
}
