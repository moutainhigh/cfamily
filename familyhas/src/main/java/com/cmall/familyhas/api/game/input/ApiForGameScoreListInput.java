package com.cmall.familyhas.api.game.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGameScoreListInput extends RootInput {
	@ZapcomApi(value="游戏编号",remark="传入游戏编号，获取游戏可玩次数",require=1)
	private String gameCode = "";
	@ZapcomApi(value="查询数量",remark="这个数值取决于取前多少名，入参如果是50，则取前50名数据，可为空，如果为空，默认取前20名",require=0,demo="20")
	private String limit = "20";
	
	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
	
}
