package com.cmall.familyhas.api.game.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGameScoreListResult extends RootResult {

	
	@ZapcomApi(value="我的排名以及分数",remark="如果用户为登陆，或者未参与游戏，返回数据为空")
	private ApiForGameScore  myScore = new ApiForGameScore();
	
	@ZapcomApi(value="用户排行",remark="参与游戏的用户得分列表")
	private List<ApiForGameScore> userScoreList = new ArrayList<ApiForGameScore>();

	public ApiForGameScore getMyScore() {
		return myScore;
	}

	public void setMyScore(ApiForGameScore myScore) {
		this.myScore = myScore;
	}

	public List<ApiForGameScore> getUserScoreList() {
		return userScoreList;
	}

	public void setUserScoreList(List<ApiForGameScore> userScoreList) {
		this.userScoreList = userScoreList;
	}
	
	
}
