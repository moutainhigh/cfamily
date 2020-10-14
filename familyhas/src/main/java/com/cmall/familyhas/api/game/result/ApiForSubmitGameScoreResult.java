package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForSubmitGameScoreResult extends RootResultWeb {

	@ZapcomApi(value="今日最高分",remark="玩家当天所获得的游戏最高分数")
	private int maxScore = 0;
	
	@ZapcomApi(value="当天玩家可玩次数",remark="玩家当天还可玩的次数")
	private int game_times = 0;

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getGame_times() {
		return game_times;
	}

	public void setGame_times(int game_times) {
		this.game_times = game_times;
	}

	
}
