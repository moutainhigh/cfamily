package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForBigWheelGetIntegralInput;
import com.cmall.familyhas.api.result.ApiForBigWheelGetIntegralResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 福利大转盘抽奖用户的积分是否够抽奖
 * @author lgx
 *
 */
public class ApiForBigWheelGetIntegral extends RootApiForToken<ApiForBigWheelGetIntegralResult, ApiForBigWheelGetIntegralInput> {

	// 大转盘抽奖消耗积分数
	private final String BigWheelDrawIntegral = bConfig("familyhas.bigWheelDrawIntegral");
	
	@Override
	public ApiForBigWheelGetIntegralResult Process(ApiForBigWheelGetIntegralInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelGetIntegralResult result = new ApiForBigWheelGetIntegralResult();
		
		String nowTime = FormatHelper.upDateTime();
		String eventCode = "";
		String memberCode = getUserCode();
		// 查询当前时间段内已经发布状态的福利大转盘活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的福利大转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 每次抽奖扣减积分数
		int drawIntegral = Integer.parseInt(BigWheelDrawIntegral);
		result.setDrawIntegral(drawIntegral);
		// 用户总积分
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		// 家有客代号
		String custId = plusServiceAccm.getCustId(memberCode);
		GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
		BigDecimal accm = new BigDecimal(0);
		if (plusModelCustAmt != null) {
			accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
		}
		int totalIntegral = accm.intValue()>0?accm.intValue():0;
		result.setTotalIntegral(totalIntegral);
		if(totalIntegral >= drawIntegral) {
			result.setIsDraw(1);
		}else {
			result.setIsDraw(0);
		}
		
		// 获取用户剩余抽奖次数
		String sql1 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
		Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sql1, new MDataMap());
		int sycs = MapUtils.getIntValue(drawNumMap, "sycs", 0);
		
		result.setRemainDrawNum(sycs);
		
		return result;
	}

}
