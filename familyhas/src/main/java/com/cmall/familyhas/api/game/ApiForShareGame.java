package com.cmall.familyhas.api.game;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.game.input.ApiForShareGameInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 用户提交所得分数接口
 * 提交一次，用户可玩次数在当前基础上减一
 * sc_hudong_game_score 表新增一条数据
 * @author Angel Joy
 *
 */
public class ApiForShareGame extends RootApiForToken<RootResultWeb,ApiForShareGameInput>{

	@Override
	public RootResultWeb Process(ApiForShareGameInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String userCode = getUserCode();
		String shareUserCode = inputParam.getShareUserCode();
		if(userCode.equals(shareUserCode)) {//自己给自己点击助理，需要返回错误值
			result.setResultCode(99999);
			result.setResultMessage("自己给自己助力无效哦");
			return result;
		}
		String gameCode = inputParam.getGameCode();
		String today = DateUtil.getSysDateString()+" 00:00:00";
		String sql  = "SELECT COUNT(1) num FROM systemcenter.sc_hudong_game_share WHERE game_code = :game_code AND share_user_code = :share_user_code AND create_time > '"+today+"' ";
		Map<String,Object> countMap = DbUp.upTable("sc_hudong_game_share").dataSqlOne(sql, new MDataMap("game_code",gameCode,"share_user_code",shareUserCode));
		Integer count = 0;
		if(countMap != null && !countMap.isEmpty()) {
			count = MapUtils.getInteger(countMap, "num", 0);
		}
		if(count > 0) {
			result.setResultCode(9992);
			result.setResultMessage("该用户今日已经被助力过，重复助力无效");
			return result;
		}
		//校验助力人是否已经超过了当日可助力次数
		String sqlCount2 = "SELECT COUNT(1) num FROM systemcenter.sc_hudong_game_share WHERE game_code = :game_code AND receive_user_code = :receive_user_code AND create_time > '"+today+"' ";
		Map<String,Object> countMap2 = DbUp.upTable("sc_hudong_game_share").dataSqlOne(sqlCount2, new MDataMap("game_code",gameCode,"receive_user_code",userCode));
		Integer count2 = 0;
		if(countMap2 != null && !countMap2.isEmpty()) {
			count2 = MapUtils.getInteger(countMap2, "num", 0);
		}
		if(count2 >= 4) {//只能主力四次
			result.setResultCode(9993);
			result.setResultMessage("已超过今日助力上限。");
			return result;
		}
		MDataMap insert = new MDataMap();
		insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		insert.put("game_code", gameCode);
		insert.put("share_user_code", shareUserCode);
		insert.put("receive_user_code", userCode);
		insert.put("create_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("sc_hudong_game_share").dataInsert(insert);
		result.setResultCode(1);
		result.setResultMessage("分享成功");
		return result;
	}
	
}
