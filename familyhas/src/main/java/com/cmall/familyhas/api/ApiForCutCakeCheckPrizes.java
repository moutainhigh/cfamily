package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForCutCakeCheckPrizesInput;
import com.cmall.familyhas.api.model.CutCakeDrawJl;
import com.cmall.familyhas.api.result.ApiForCutCakeCheckPrizesResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 切蛋糕查看奖品接口
 * @author lgx
 *
 */
public class ApiForCutCakeCheckPrizes extends RootApiForToken<ApiForCutCakeCheckPrizesResult, ApiForCutCakeCheckPrizesInput> {

	
	public ApiForCutCakeCheckPrizesResult Process(ApiForCutCakeCheckPrizesInput inputParam, MDataMap mRequestMap) {
		
		ApiForCutCakeCheckPrizesResult result = new ApiForCutCakeCheckPrizesResult();
		
		List<CutCakeDrawJl> list = new ArrayList<CutCakeDrawJl>();
		
		String memberCode = getUserCode();
		String nowTime = FormatHelper.upDateTime();
		String eventCode = "";
		
		// 查询当前时间段内已经发布状态的切蛋糕活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210011' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的切蛋糕活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 查询该用户切蛋糕奖品记录
		String sql = "SELECT * FROM sc_hudong_cake_draw_jl WHERE event_code = '"+eventCode+"' AND member_code = '"+memberCode+"' ORDER BY create_time DESC";
		List<Map<String, Object>> cakeDrawList = DbUp.upTable("sc_hudong_cake_draw_jl").dataSqlList(sql, new MDataMap());
		if(cakeDrawList != null && cakeDrawList.size() > 0) {
			for (Map<String, Object> map : cakeDrawList) {
				CutCakeDrawJl cutCakeDrawJl = new CutCakeDrawJl();
				String create_time = MapUtils.getString(map, "create_time").substring(0, 10);
				String jp_type = MapUtils.getString(map, "jp_type");
				String jp_name = MapUtils.getString(map, "jp_name");
				cutCakeDrawJl.setCreateTime(create_time);
				cutCakeDrawJl.setJpName(jp_name);
				cutCakeDrawJl.setJpType(jp_type);
				
				list.add(cutCakeDrawJl);
			}
		}
		
		result.setList(list);
		
		return result;
	}


}
