package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;


import com.cmall.familyhas.api.input.ApiForJFBigWheelJfExchangeInput;
import com.cmall.familyhas.api.result.ApiForJFBigWheelJfExchangeResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分大转盘兑换抽奖次数接口
 * @author lgx
 *
 */
public class ApiForJFBigWheelJfExchange extends RootApiForToken<ApiForJFBigWheelJfExchangeResult, ApiForJFBigWheelJfExchangeInput> {
	
	// 积分转盘兑换抽奖次数时消耗的积分数(200)
	private final String jf_bigwheel_need_jfnum = bConfig("familyhas.jf_bigwheel_need_jfnum");
	// 积分转盘每次兑换的抽奖次数(5)
	private final String jf_bigWheel_drawnum = bConfig("familyhas.jf_bigWheel_drawnum");
	
	@Override
	public ApiForJFBigWheelJfExchangeResult Process(ApiForJFBigWheelJfExchangeInput inputParam, MDataMap mRequestMap) {
		ApiForJFBigWheelJfExchangeResult result = new ApiForJFBigWheelJfExchangeResult();
		
		int jfnum = Integer.parseInt(jf_bigwheel_need_jfnum);
		
		String memberCode = getUserCode();
		String eventCode = "";
		
		String nowTime = FormatHelper.upDateTime();
		//String startTime = nowTime.substring(0, 10)+" 00:00:00";
		//String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的积分转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 查询用户的总积分
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		// 家有客代号
		String custId = plusServiceAccm.getCustId(memberCode);
		GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
		//DecimalFormat df = new DecimalFormat("#.#");
		BigDecimal accm = new BigDecimal(0);
		if (plusModelCustAmt != null) {
			accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
		}
		if(accm.compareTo(new BigDecimal(jfnum)) >= 0) {
			// 扣积分兑换抽奖次数
			// 积分转钱
			BigDecimal jfNum = new BigDecimal(jfnum);
			BigDecimal giveMoney = plusServiceAccm.accmAmtToMoney(jfNum,2);
			RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.DZC, giveMoney, custId, "", "DD"+eventCode);
			// 记录积分变更日志  
			if(teamResult.getResultCode() == 1) {
				// 增加抽奖次数
				String drawsql = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
				Map<String, Object> drawMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(drawsql, new MDataMap());
				MDataMap mDataMap4 = new MDataMap();				
				mDataMap4.put("uid", (String) drawMap.get("uid"));
				mDataMap4.put("sycs", jf_bigWheel_drawnum);
				mDataMap4.put("update_time", nowTime);
				DbUp.upTable("sc_huodong_event_sycs").dataUpdate(mDataMap4, "sycs,update_time", "uid");
				result.setIsSuccess("Y");
				accm = accm.subtract(new BigDecimal(jfnum));
				
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("member_code", memberCode);
				mDataMap.put("cust_id", custId);
				mDataMap.put("change_type", "449748080014");
				mDataMap.put("change_money", giveMoney.toString());
				mDataMap.put("remark", eventCode);
				mDataMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
			}else {
				result.setIsSuccess("N");
				result.setResultCode(-1);
				result.setResultMessage("积分兑换次数失败!");
			}
		}else {
			// 提示积分不够
			result.setIsSuccess("N");
			result.setResultCode(-1);
			result.setResultMessage("您的账户积分不足，请去下单赚积分!");
		}
		
		// 获取用户剩余抽奖次数
		String sql1 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
		Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sql1, new MDataMap());
		int sycs = (int) drawNumMap.get("sycs");
		result.setRemainDrawNum(sycs);
		
		// 用户剩余积分
		result.setIntegralTotal(accm.intValue());
		
		return result;
	}

}
