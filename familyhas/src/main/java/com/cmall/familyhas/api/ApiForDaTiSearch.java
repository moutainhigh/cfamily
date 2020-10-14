package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.DaTiSearchInput;
import com.cmall.familyhas.api.model.Title;
import com.cmall.familyhas.api.result.DaTiSearchResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiForDaTiSearch extends RootApiForToken<DaTiSearchResult, DaTiSearchInput> {

	@Override
	public DaTiSearchResult Process(DaTiSearchInput inputParam, MDataMap mRequestMap) {
		DaTiSearchResult result = new DaTiSearchResult();
		String nowTime = DateUtil.getNowTime();
		String sql1 = "select b.* from sc_hudong_event_hongbao_info a left join sc_hudong_event_info b on a.event_code = b.event_code"
				+ " where b.event_type_code = '449748210002' and b.event_status = '4497472700020002' and b.begin_time < '"+nowTime+"' and b.end_time > '"+ nowTime + "'";
		List<Map<String, Object>> dataSqlList1 = DbUp.upTable("sc_hudong_event_hongbao_info").dataSqlList(sql1, null);
		if (null != dataSqlList1 && dataSqlList1.size() > 0) {//当前时间有抽奖活动
			result.setHasActivity(1);
			Map<String, Object> map = dataSqlList1.get(0);
			//判断当前用户是否参加过此答题活动
			// 获取用户编号
			String memberCode = getUserCode();
			//获取当前答题活动
			String eventCode = map.get("event_code").toString();
			result.setEventCode(eventCode);//返回参数中加上活动编号
			String sql2 = "select * from sc_hudong_event_dati_log where member_code ='"+memberCode+"' and event_code = '"+eventCode+"'";
			List<Map<String, Object>> dataSqlList2 = DbUp.upTable("sc_hudong_event_dati_log").dataSqlList(sql2, null);
			if (null != dataSqlList2 && dataSqlList2.size() > 0) {//用户已答过此题
				result.setResultCode(0);
			}else {//用户未答过此题
				result.setIsJoin(1);
				//查询题目信息
				String sql3 = "select * from sc_hudong_event_dati_topic where event_code  = '"+eventCode+"'";
				List<Map<String, Object>> dataSqlList3 = DbUp.upTable("sc_hudong_event_dati_topic").dataSqlList(sql3, null);
				if (null != dataSqlList3 && dataSqlList3.size() > 0) {
					List<Title> list = new ArrayList<>();
					for(Map<String, Object> map2 : dataSqlList3) {
						Title title = new  Title();
						title.setQuestion(map2.get("question").toString());
						title.setAnswerA(map2.get("answer1").toString());
						title.setAnswerB(map2.get("answer2").toString());
						title.setAnswerC(map2.get("answer3").toString());
						title.setAnswerD(map2.get("answer4").toString());
						title.setBingo(map2.get("bingo").toString());
						list.add(title);
					}
					result.setTitles(list);
				}
			}
		}else {//当前时间无抽奖活动
			result.setResultCode(0);
		}
		return result;
	}
}
