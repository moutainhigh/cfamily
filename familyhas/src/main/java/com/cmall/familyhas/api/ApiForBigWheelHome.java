package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.cmall.familyhas.api.input.ApiForBigWheelHomeInput;
import com.cmall.familyhas.api.model.HudongEventInfo;
import com.cmall.familyhas.api.model.HuodongEventDzpjpRule;
import com.cmall.familyhas.api.result.ApiForBigWheelHomeResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 大转盘页面获取数据接口
 * @author lgx
 *
 */
public class ApiForBigWheelHome extends RootApiForVersion<ApiForBigWheelHomeResult, ApiForBigWheelHomeInput> {

	// 大转盘初始化抽奖次数
	private final String BigWheelOriginalDrawNum = bConfig("familyhas.bigWheelOriginalDrawNum");
	// 大转盘分享获得最大抽奖次数
	//private final String BigWheelShareDrawNum = bConfig("familyhas.bigWheelShareDrawNum");
	// 大转盘抽奖消耗积分数
	private final String BigWheelDrawIntegral = bConfig("familyhas.bigWheelDrawIntegral");
	
	@Override
	public ApiForBigWheelHomeResult Process(ApiForBigWheelHomeInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelHomeResult result = new ApiForBigWheelHomeResult();
		
		String nowTime = FormatHelper.upDateTime();
		//String startTime = nowTime.substring(0, 10)+" 00:00:00";
		//String endTime = nowTime.substring(0, 10)+" 23:59:59";
		int drawIntegral = Integer.parseInt(BigWheelDrawIntegral);;
		int totalIntegral = 0;
		
		// 查询当前时间段内已经发布状态的大转盘活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的福利转盘活动!");
			return result;
		}else {
			// 大转盘活动信息
			HudongEventInfo hudongEventInfo = new HudongEventInfo();
			hudongEventInfo.setEventCode((String) eventInfoMap.get("event_code"));
			hudongEventInfo.setEventName((String) eventInfoMap.get("event_name"));
			hudongEventInfo.setEventTypeCode((String) eventInfoMap.get("event_type_code"));
			hudongEventInfo.setEventStatus((String) eventInfoMap.get("event_status"));
			hudongEventInfo.setBeginTime((String) eventInfoMap.get("begin_time"));
			hudongEventInfo.setEndTime((String) eventInfoMap.get("end_time"));
			hudongEventInfo.setDzpShareTitle((String) eventInfoMap.get("dzp_share_title"));
			hudongEventInfo.setDzpShareDesc((String) eventInfoMap.get("dzp_share_desc"));
			result.setHudongEventInfo(hudongEventInfo);
			
			// 大转盘奖品信息
			String sSql2 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_status = '1' AND event_code = '" + eventInfoMap.get("event_code") + "'";
			List<Map<String, Object>> eventDzpjpList = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlList(sSql2, new MDataMap());
			List<HuodongEventDzpjpRule> list = new ArrayList<HuodongEventDzpjpRule>();
			if(eventDzpjpList != null && eventDzpjpList.size() > 0) {
				for (Map<String, Object> map : eventDzpjpList) {
					HuodongEventDzpjpRule huodongEventDzpjpRule = new HuodongEventDzpjpRule();
					huodongEventDzpjpRule.setEventCode((String) map.get("event_code"));
					huodongEventDzpjpRule.setJpCode((String) map.get("jp_code"));
					huodongEventDzpjpRule.setJpTitle((String) map.get("jp_title"));
					huodongEventDzpjpRule.setJpType((String) map.get("jp_type"));
					//huodongEventDzpjpRule.setJpNum((int) map.get("jp_num"));
					//huodongEventDzpjpRule.setJpZjgl((int) map.get("jp_zjgl"));
					//huodongEventDzpjpRule.setProductName((String) map.get("product_name"));
					huodongEventDzpjpRule.setJpImg((String) map.get("jp_img"));
					//huodongEventDzpjpRule.setJfNum((int) map.get("jf_num"));
					//huodongEventDzpjpRule.setCouponTypeCode((String) map.get("coupon_type_code"));
					list.add(huodongEventDzpjpRule);
				}
				result.setList(list);
				
			}else {
				result.setResultCode(-1);
				result.setResultMessage("当前福利转盘活动没有奖品信息!");
				return result;
			}
		}
		
		// 根据用户编号和当前时间获取剩余抽奖次数
		if (getFlagLogin()) {
			//MOauthInfo oauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token"));
			MOauthInfo oauthInfo = getOauthInfo();
			if (oauthInfo != null) {
				// 直接查询剩余抽奖次数
				String sSql3 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ oauthInfo.getUserCode() +"' AND event_code = '" + eventInfoMap.get("event_code") + "'";
				Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sSql3, new MDataMap());
				if(drawNumMap == null) {
					// 查不到说明是第一次登录进入抽奖页面,初始化抽奖次数2次
					MDataMap eventSycsMap = new MDataMap();
					eventSycsMap.put("uid", WebHelper.upUuid());
					eventSycsMap.put("member_code", oauthInfo.getUserCode());
					eventSycsMap.put("sycs", BigWheelOriginalDrawNum);
					eventSycsMap.put("update_time", nowTime);
					eventSycsMap.put("event_code", (String) eventInfoMap.get("event_code"));
					DbUp.upTable("sc_huodong_event_sycs").dataInsert(eventSycsMap);
					result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
				}else {
					// 查到了,看更新时间
					String updateTime = drawNumMap.get("update_time").toString().substring(0, 10);
					if(nowTime.substring(0, 10).equals(updateTime)) {
						// 更新时间是今天
						result.setRemainDrawNum((int) drawNumMap.get("sycs"));
					}else {
						// 不是今天,更新次数初始化为2次,并更新时间
						MDataMap eventSycsMap = new MDataMap();
						eventSycsMap.put("uid", drawNumMap.get("uid").toString());
						eventSycsMap.put("sycs", BigWheelOriginalDrawNum);
						eventSycsMap.put("update_time", nowTime);
						DbUp.upTable("sc_huodong_event_sycs").dataUpdate(eventSycsMap, "sycs,update_time", "uid");
						result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
					}
				}
				
				// 用户总积分
				PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
				// 家有客代号
				String custId = plusServiceAccm.getCustId(oauthInfo.getUserCode());
				GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
				BigDecimal accm = new BigDecimal(0);
				if (plusModelCustAmt != null) {
					accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
				}
				totalIntegral = accm.intValue()>0?accm.intValue():0;
				
			}else {
				result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
			}
		}else {
			result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
		}
		
		result.setDrawIntegral(drawIntegral);
		result.setTotalIntegral(totalIntegral);
		
		return result;
	}

}
