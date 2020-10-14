package com.cmall.familyhas.api.game.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForShareGameInput extends RootInput {
	@ZapcomApi(value="游戏编号",remark="传入游戏编号",require=1)
	private String gameCode = "";
	
	@ZapcomApi(value="分享人用户编号",remark="分享人用户编号",require=1)
	private String shareUserCode = "";

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getShareUserCode() {
		return shareUserCode;
	}

	public void setShareUserCode(String shareUserCode) {
		this.shareUserCode = shareUserCode;
	}

	
	
}
