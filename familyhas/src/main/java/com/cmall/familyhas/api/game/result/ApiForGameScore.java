package com.cmall.familyhas.api.game.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiForGameScore {
	@ZapcomApi(value="昵称",remark="用户昵称")
	private String nickName = "";
	
	@ZapcomApi(value="头像",remark="用户头像")
	private String avatar = "";
	
	@ZapcomApi(value="今日最高分",remark="今日最高分数")
	private String score = "";
	
	@ZapcomApi(value="排名",remark="名次")
	private String Ranking = "";

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRanking() {
		return Ranking;
	}

	public void setRanking(String ranking) {
		Ranking = ranking;
	}

	
}
