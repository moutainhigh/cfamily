package com.cmall.familyhas.api.game.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSubmitGameScoreInput extends RootInput {
	@ZapcomApi(value="游戏编号",remark="传入游戏编号",require=1)
	private String gameCode = "";
	
	@ZapcomApi(value="游戏得分",remark="玩家游戏分数",require=1)
	private int score = 0;
	
	@ZapcomApi(value="提交类型",remark="减次数还是提交分数，1：减次数，2：分数提交",require=1)
	private int type = 1;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
