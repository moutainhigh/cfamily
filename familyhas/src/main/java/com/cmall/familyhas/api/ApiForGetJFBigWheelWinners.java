package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetJFWinnersInput;
import com.cmall.familyhas.api.model.JFBigWheelWinner;
import com.cmall.familyhas.api.result.ApiForGetJFWinnersResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 获取积分大转盘页面滚动获奖名单接口
 * @author lgx
 *
 */
public class ApiForGetJFBigWheelWinners extends RootApiForToken<ApiForGetJFWinnersResult, ApiForGetJFWinnersInput> {

	@Override
	public ApiForGetJFWinnersResult Process(ApiForGetJFWinnersInput inputParam, MDataMap mRequestMap) {
		ApiForGetJFWinnersResult result = new ApiForGetJFWinnersResult();
		
		List<JFBigWheelWinner> list = new ArrayList<JFBigWheelWinner>();
		String eventCode = "";
		// 奖品总人数
		int totalNum = 0;
		
		// 确定当前时间段内已经发布状态的积分大转盘活动(1条)
		String nowTime = FormatHelper.upDateTime();
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的积分转盘活动!");
			return result;
		}else {
			// 查询抽中全部5个字的用户
			eventCode = (String) eventInfoMap.get("event_code");
			String sql1 = "SELECT * FROM lc_huodong_event_jl WHERE event_code = '"+eventCode+"' AND jp_title = '惠' ORDER BY zj_time DESC";
			List<Map<String, Object>> jlList = DbUp.upTable("lc_huodong_event_jl").dataSqlList(sql1, new MDataMap());
			if(jlList != null && jlList.size() > 0) {
				for (Map<String, Object> map : jlList) {
						// 取最新的50条数据
						String memberCode = (String) map.get("member_code");
						int count1 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '我'", new MDataMap());
						int count2 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '爱'", new MDataMap());
						//int count3 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '惠'", new MDataMap());
						int count4 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '家'", new MDataMap());
						int count5 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '有'", new MDataMap());
						if(count1 > 0 && count2 > 0 && count4 > 0 &&count5 > 0) {						
							if(list.size() < 50) {
								JFBigWheelWinner bigWheelWinner = new JFBigWheelWinner();
								bigWheelWinner.setNickname(map.get("nickname").toString());
								list.add(bigWheelWinner);
							}
							totalNum += 1;
						}
				}
				result.setList(list);
				// 添加集齐"我爱惠家有"的人数(lgx/20191029)
				Map<String, Object> winnerNumMap = DbUp.upTable("zw_define").dataSqlOne("SELECT * FROM zw_define WHERE define_dids = '469923300001'", new MDataMap());
				int winnerNum = 0;
				if(winnerNumMap != null && winnerNumMap.get("define_remark") != null) {
					winnerNum = Integer.parseInt((String) winnerNumMap.get("define_remark"));
				}
				totalNum += winnerNum;
				result.setTotalNum(totalNum);
			}else {
				result.setResultCode(-1);
				result.setResultMessage("没有获奖名单!");
				return result;
			}
		}
		
		return result;
	}
	
}
