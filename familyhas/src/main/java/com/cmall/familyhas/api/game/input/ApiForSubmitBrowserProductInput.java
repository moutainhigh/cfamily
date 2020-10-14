package com.cmall.familyhas.api.game.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForSubmitBrowserProductInput extends RootInput {
	
	@ZapcomApi(value="游戏编号",remark="传入游戏编号，获取游戏可玩次数",require=1)
	private String gameCode = "";

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	

}
