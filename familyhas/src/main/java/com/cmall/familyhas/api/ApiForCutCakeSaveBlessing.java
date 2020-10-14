package com.cmall.familyhas.api;

import java.util.Map;

import com.cmall.familyhas.api.input.ApiForCutCakeSaveBlessingInput;
import com.cmall.familyhas.api.result.ApiForCutCakeSaveBlessingResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 保存用户切蛋糕祝福语
 * @author lgx
 *
 */
public class ApiForCutCakeSaveBlessing extends RootApiForToken<ApiForCutCakeSaveBlessingResult, ApiForCutCakeSaveBlessingInput> {

	@Override
	public ApiForCutCakeSaveBlessingResult Process(ApiForCutCakeSaveBlessingInput inputParam, MDataMap mRequestMap) {
		
		ApiForCutCakeSaveBlessingResult result = new ApiForCutCakeSaveBlessingResult();
		// 用户的切蛋糕祝福语
		String cakeBlessing = inputParam.getCakeBlessing();
		
		String nowTime = FormatHelper.upDateTime();
		String startTime = nowTime.substring(0, 10)+" 00:00:00";
		String endTime = nowTime.substring(0, 10)+" 23:59:59";
		String memberCode = getUserCode();
		
		// 查询当前时间段内已经发布状态的切蛋糕活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210011' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的切蛋糕活动!");
			return result;
		}
		
		// 查询当天是否已经发表过祝福,如果已经发表过祝福则不能再发表
		String blessSql = "SELECT * FROM sc_hudong_cake_blessing WHERE member_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"'";
		Map<String, Object> cake_blessing = DbUp.upTable("sc_hudong_cake_blessing").dataSqlOne(blessSql, new MDataMap());
		if(cake_blessing != null) {
			// 今天已经送祝福
			result.setResultCode(-1);
			result.setResultMessage("今日已经送过祝福!");
			return result;
		}else {
			// 今天没有送祝福,则过滤保存祝福
			MDataMap mDataMap5 = new MDataMap();
			mDataMap5.put("uid", WebHelper.upUuid());
			mDataMap5.put("member_code", memberCode);
			mDataMap5.put("cake_blessing", cakeBlessing);
			mDataMap5.put("check_status", "449748580002");
			mDataMap5.put("create_time", nowTime);
			mDataMap5.put("is_draw", "0");
			DbUp.upTable("sc_hudong_cake_blessing").dataInsert(mDataMap5);
		}
		
		return result;
	}

}
