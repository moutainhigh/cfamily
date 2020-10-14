package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.cmall.familyhas.api.input.ApiForJFBigWheelHomeInput;
import com.cmall.familyhas.api.model.HudongEventInfo;
import com.cmall.familyhas.api.model.HuodongEventDzpjfRule;
import com.cmall.familyhas.api.result.ApiForJFBigWheelHomeResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分大转盘页面获取数据接口
 * @author lgx
 *
 */
public class ApiForJFBigWheelHome extends RootApiForToken<ApiForJFBigWheelHomeResult, ApiForJFBigWheelHomeInput> {

	@Override
	public ApiForJFBigWheelHomeResult Process(ApiForJFBigWheelHomeInput inputParam, MDataMap mRequestMap) {
		ApiForJFBigWheelHomeResult result = new ApiForJFBigWheelHomeResult();
		
		String nowTime = FormatHelper.upDateTime();
		String memberCode = getUserCode();
		String eventCode = "";
		// 查询当前时间段内已经发布状态的积分大转盘活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的积分大转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
			// 大转盘活动信息
			HudongEventInfo hudongEventInfo = new HudongEventInfo();
			hudongEventInfo.setEventCode(eventCode);
			hudongEventInfo.setEventName((String) eventInfoMap.get("event_name"));
			hudongEventInfo.setEventTypeCode((String) eventInfoMap.get("event_type_code"));
			hudongEventInfo.setEventStatus((String) eventInfoMap.get("event_status"));
			hudongEventInfo.setBeginTime((String) eventInfoMap.get("begin_time"));
			hudongEventInfo.setEndTime((String) eventInfoMap.get("end_time"));
			hudongEventInfo.setDzpShareTitle((String) eventInfoMap.get("dzp_share_title"));
			hudongEventInfo.setDzpShareDesc((String) eventInfoMap.get("dzp_share_desc"));
			result.setHudongEventInfo(hudongEventInfo);
			
			// 大转盘奖品信息
			String sSql2 = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode + "'";
			List<Map<String, Object>> eventDzpjpList = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlList(sSql2, new MDataMap());
			List<HuodongEventDzpjfRule> list = new ArrayList<HuodongEventDzpjfRule>();
			if(eventDzpjpList != null && eventDzpjpList.size() > 0) {
				for (Map<String, Object> map : eventDzpjpList) {
					HuodongEventDzpjfRule huodongEventDzpjfRule = new HuodongEventDzpjfRule();
					huodongEventDzpjfRule.setEventCode((String) map.get("event_code"));
					huodongEventDzpjfRule.setJpCode((String) map.get("jp_code"));
					huodongEventDzpjfRule.setJpTitle((String) map.get("jp_title"));
					huodongEventDzpjfRule.setJpImg((String) map.get("jp_img"));
					list.add(huodongEventDzpjfRule);
				}
				result.setList(list);
				
			}else {
				result.setResultCode(-1);
				result.setResultMessage("当前积分转盘活动没有奖品信息!");
				return result;
			}
			
			// 用户总积分
			PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
			// 家有客代号
			String custId = plusServiceAccm.getCustId(memberCode);
			GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
			//DecimalFormat df = new DecimalFormat("#.#");
			if (plusModelCustAmt != null) {
				BigDecimal accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
				result.setIntegralTotal(accm.intValue()>0?accm.intValue():0);
			}
			
			// 用户可用抽奖次数
			String sSql3 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
			Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sSql3, new MDataMap());
			if(drawNumMap == null) {
				// 查不到说明是第一次登录进入抽奖页面
				MDataMap eventSycsMap = new MDataMap();
				eventSycsMap.put("uid", WebHelper.upUuid());
				eventSycsMap.put("member_code", memberCode);
				eventSycsMap.put("sycs", "0");
				eventSycsMap.put("update_time", nowTime);
				eventSycsMap.put("event_code", eventCode);
				DbUp.upTable("sc_huodong_event_sycs").dataInsert(eventSycsMap);
				result.setRemainDrawNum(0);
			}else {
				result.setRemainDrawNum((int) drawNumMap.get("sycs"));
			}
			
			// 每个字抽中个数
			int count1 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '我'", new MDataMap());
			int count2 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '爱'", new MDataMap());
			int count3 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '惠'", new MDataMap());
			int count4 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '家'", new MDataMap());
			int count5 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '有'", new MDataMap());
			result.setCount1(count1);
			result.setCount2(count2);
			result.setCount3(count3);
			result.setCount4(count4);
			result.setCount5(count5);
		}
		
		return result;
	}

}
