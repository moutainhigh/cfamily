package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetWinnersInput;
import com.cmall.familyhas.api.model.BigWheelWinner;
import com.cmall.familyhas.api.result.ApiForGetWinnersResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取大转盘页面滚动获奖名单接口
 * @author lgx
 *
 */
public class ApiForGetBigWheelWinners extends RootApiForVersion<ApiForGetWinnersResult, ApiForGetWinnersInput> {


	public ApiForGetWinnersResult Process(ApiForGetWinnersInput inputParam, MDataMap mRequestMap) {
		ApiForGetWinnersResult result = new ApiForGetWinnersResult();
		
		List<BigWheelWinner> list = new ArrayList<BigWheelWinner>();
				
		// 确定当前时间段内已经发布状态的大转盘活动(1条)
		String nowTime = FormatHelper.upDateTime();
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的福利转盘活动!");
			return result;
		}else {
			String sql1 = "SELECT * FROM lc_huodong_event_jl WHERE event_code = '"+eventInfoMap.get("event_code")+"' AND jp_type in ('4497471600470001','4497471600470002','4497471600470003') ORDER BY zj_time DESC LIMIT 50";
			List<Map<String, Object>> jlList = DbUp.upTable("lc_huodong_event_jl").dataSqlList(sql1, new MDataMap());
			if(jlList != null && jlList.size() > 0) {
				for (Map<String, Object> map : jlList) {
					BigWheelWinner bigWheelWinner = new BigWheelWinner();
					//String sql2 = "SELECT * FROM mc_member_sync WHERE member_code = '"+map.get("member_code")+"'";
					//Map<String, Object> member = DbUp.upTable("mc_member_sync").dataSqlOne(sql2, new MDataMap());
					bigWheelWinner.setJpTitle(map.get("jp_title").toString());
					bigWheelWinner.setNickname(map.get("nickname").toString());
					
					list.add(bigWheelWinner);
				}
				result.setList(list);
			}else {
				result.setResultCode(-1);
				result.setResultMessage("没有获奖名单!");
				return result;
			}
		}
		
		return result;
	}
	
}
