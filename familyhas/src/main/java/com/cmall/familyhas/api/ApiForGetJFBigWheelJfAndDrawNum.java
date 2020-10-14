package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetJFBigWheelJfAndDrawNumInput;
import com.cmall.familyhas.api.result.ApiForGetJFBigWheelJfAndDrawNumResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 获取积分转盘抽奖用户的积分信息和抽奖次数接口
 * @author lgx
 *
 */
public class ApiForGetJFBigWheelJfAndDrawNum extends RootApiForToken<ApiForGetJFBigWheelJfAndDrawNumResult, ApiForGetJFBigWheelJfAndDrawNumInput> {

	@Override
	public ApiForGetJFBigWheelJfAndDrawNumResult Process(ApiForGetJFBigWheelJfAndDrawNumInput inputParam, MDataMap mRequestMap) {
		ApiForGetJFBigWheelJfAndDrawNumResult result = new ApiForGetJFBigWheelJfAndDrawNumResult();
		
		String nowTime = FormatHelper.upDateTime();
		String eventCode = "";
		String memberCode = getUserCode();
		// 查询当前时间段内已经发布状态的积分大转盘活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的积分大转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
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
		
		return result;
	}

}
