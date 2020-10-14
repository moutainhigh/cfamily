package com.cmall.familyhas.api.game.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetTaskAwardInput extends RootInput {
	@ZapcomApi(value="游戏编号",remark="传入游戏编号，获取游戏可玩次数",require=1)
	private String gameCode = "";
	
	@ZapcomApi(value="任务类型",remark="4497471600530001：邀请好友喂青蛙，4497471600530002：邀请好友下载APP，4497471600530003：下单购买，4497471600530004：浏览X分钟",demo="4497471600530001",require=1)
	private String taskType = "4497471600530001";

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
}
